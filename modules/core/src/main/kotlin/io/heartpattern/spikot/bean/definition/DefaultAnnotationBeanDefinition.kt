/*
 * Copyright (c) 2020 HeartPattern and Spikot authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.heartpattern.spikot.bean.definition

import io.heartpattern.spikot.annotation.MergedAnnotations
import io.heartpattern.spikot.annotation.mergedAnnotations
import io.heartpattern.spikot.bean.*
import io.heartpattern.spikot.condition.Condition
import io.heartpattern.spikot.condition.ConditionContext
import io.heartpattern.spikot.condition.ConditionEvaluator
import io.heartpattern.spikot.reflection.getObjectInstanceOrCreate
import io.heartpattern.spikot.reflection.withAccessibility
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.*
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaField

public class DefaultAnnotationBeanDefinition private constructor(
    public override val type: KClass<*>,
    public override val annotations: MergedAnnotations,
    private val constructor: (List<Any>) -> Any,
    constructorParameters: List<KParameter>,
    defaultName: String
) : BeanDefinition {
    override val scope: String = annotations.get<Scope>()?.getTypedAttribute<String>("name") ?: "singleton"
    override val name: String = annotations.get<Qualifier>()?.getTypedAttribute<String>("name") ?: defaultName
    override val loadOrder: Int = annotations.get<LoadOrder>()?.getTypedAttribute<Int>("order") ?: 0
    override val isPrimary: Boolean = annotations.has<Primary>()
    override val isLazy: Boolean = annotations.has<Lazy>()
    override val dependsOn: Set<BeanDescription>

    private val explicitDependencies: Set<BeanDescription>
    private val initializeFunctions: Collection<KFunction<*>>
    private val destroyFunctions: Collection<KFunction<*>>
    private val propertyInjects: Collection<Pair<BeanDescription, PropertyInjector>>
    private val constructorInjects: List<BeanDescription>
    private val conditionHandlers: Collection<KClass<out ConditionEvaluator>>

    init {
        // Explicit dependency by DependsOn (Kotlin object support)
        explicitDependencies = parseExplicitDependencies()

        initializeFunctions = parseInitializeFunctions()
        destroyFunctions = parseDestroyFunctions()
        conditionHandlers = parseConditions()
        propertyInjects = parsePropertyInjects()
        constructorInjects = parseConstructor(constructorParameters)
        dependsOn = explicitDependencies + constructorInjects + propertyInjects.map { it.first }
    }

    override fun create(beanRegistry: BeanRegistry): Any {
        // Check existence of explicit dependencies
        explicitDependencies.forEach(beanRegistry::getBean)

        val constructorParameter = constructorInjects.map(beanRegistry::getBean)
        val instance = constructor(constructorParameter)

        injectProperty(instance, beanRegistry)

        return instance
    }

    override fun injectProperty(bean: Any, beanRegistry: BeanRegistry) {
        for ((descriptor, injector) in propertyInjects) {
            val injected = beanRegistry.getBean(descriptor)
            injector.inject(bean, injected)
        }
    }

    override fun checkCondition(conditionContext: ConditionContext): Boolean {
        for (condition in conditionHandlers) {
            val conditionInstance = condition.getObjectInstanceOrCreate()
            if (!conditionInstance.check(this, conditionContext)) {
                return false
            }
        }

        return true
    }

    override fun invokeInitializeFunction(bean: Any) {
        for (function in initializeFunctions) {
            function.withAccessibility {
                function.call(bean)
            }
        }
    }

    override fun invokeDestroyFunction(bean: Any) {
        for (function in destroyFunctions) {
            function.withAccessibility {
                function.call(bean)
            }
        }
    }

    private fun parseExplicitDependencies(): Set<BeanDescription> {
        return annotations.getAll<DependsOn>()
            .flatMap { annotation ->
                annotation.getTypedAttribute<Array<String>>("dependencies")
                    .map(BeanDescription.Companion::fromName)
            }.toSet()
    }

    private fun parsePropertyInjects(): Collection<Pair<BeanDescription, PropertyInjector>> {
        val propertyInject = LinkedList<Pair<BeanDescription, PropertyInjector>>()

        for (property in type.memberProperties) {
            val annotations = property.mergedAnnotations

            val backingField = property.javaField

            if (!annotations.has<Inject>() && backingField?.type?.kotlin != InjectProperty::class)
                continue

            if (backingField == null)
                throw IllegalArgumentException("Cannot inject to computed property")

            if (backingField.type.kotlin != property.returnType.classifier &&
                backingField.type.kotlin != InjectProperty::class)
                throw IllegalArgumentException("Cannot inject to unknown delegate property")

            val propertyType = property.returnType.kotlinClass
                ?: throw InjectException(type, property.name, "Cannot inject value to generic property")
            val beanName = annotations.resolveBeanName()
            val beanDescription = BeanDescription.fromTypeAndName(propertyType, beanName)

            if (backingField.type.kotlin != InjectProperty::class) { // Field injection
                propertyInject += beanDescription to PropertyInjector { bean, value ->
                    backingField.withAccessibility {
                        backingField[bean] = value
                    }
                }
            } else {
                propertyInject += beanDescription to PropertyInjector { bean, value ->
                    @Suppress("UNCHECKED_CAST")
                    property.withAccessibility {
                        val delegate = (property as KProperty1<Any, Any>).getDelegate(bean)
                        if (delegate !is InjectProperty<*>)
                            throw InjectException(type, property.name, "Cannot inject value to unknown delegate")

                        delegate.value = value
                    }
                }
            }
        }

        return propertyInject
    }

    private fun parseConstructor(parameters: List<KParameter>): List<BeanDescription> {
        val constructorInject = ArrayList<BeanDescription>(parameters.size)

        for ((index, parameter) in parameters.withIndex()) {
            val name = parameter.mergedAnnotations.resolveBeanName()
            val type = parameter.type.kotlinClass
                ?: throw InjectException(
                    type,
                    parameter.name ?: "arg$index",
                    "Cannot inject value to generic parameter"
                )
            constructorInject += BeanDescription.fromTypeAndName(type, name)
        }

        return constructorInject
    }

    private fun parseInitializeFunctions(): Collection<KFunction<*>> {
        val declared = type.memberFunctions.filter { func ->
            val mergedAnnotations = func.mergedAnnotations
            mergedAnnotations.has<AfterInitialize>()
        }.toSet()
        val mark = annotations.get<Bean>()?.getTypedAttribute<String>("initialize")

        return if (mark.isNullOrEmpty()) {
            declared
        } else {
            val markFunction = type.memberFunctions.find { it.name == mark }
                ?: throw NoSuchMethodException("Cannot find function $mark in $type")

            declared + markFunction
        }
    }

    private fun parseDestroyFunctions(): Collection<KFunction<*>> {
        val declared = type.memberFunctions.filter { func ->
            val mergedAnnotations = func.mergedAnnotations
            mergedAnnotations.has<BeforeDestroy>()
        }.toSet()
        val mark = annotations.get<Bean>()?.getTypedAttribute<String>("destroy")

        return if (mark.isNullOrEmpty()) {
            declared
        } else {
            val markFunction = type.memberFunctions.find { it.name == mark }
                ?: throw NoSuchMethodException("Cannot find function $mark in $type")

            declared + markFunction
        }
    }

    override fun toString(): String {
        return "DefaultAnnotationBeanDefinition(type=$type, scope='$scope', name='$name', loadOrder=$loadOrder, isPrimary=$isPrimary, isLazy=$isLazy)"
    }

    private fun parseConditions(): Collection<KClass<out ConditionEvaluator>> {
        return annotations.getAll<Condition>()
            .map { it.getTypedAttribute<Class<out ConditionEvaluator>>("evaluator").kotlin }
    }

    private fun interface PropertyInjector {
        fun inject(bean: Any, value: Any)
    }

    public companion object {
        public fun fromClass(type: KClass<*>): DefaultAnnotationBeanDefinition {
            val annotations = type.mergedAnnotations
            val defaultName = type.resolveBeanName()
                ?: throw IllegalArgumentException("Cannot determine default bean name")

            if (type.objectInstance != null) {
                return DefaultAnnotationBeanDefinition(
                    type,
                    annotations,
                    { type.objectInstance!! },
                    emptyList(),
                    defaultName
                )
            } else {
                val constructor = resolveConstructor(type)

                return DefaultAnnotationBeanDefinition(
                    type,
                    annotations,
                    { args ->
                        constructor.withAccessibility {
                            constructor.call(*args.toTypedArray())!!
                        }
                    },
                    constructor.valueParameters,
                    defaultName
                )
            }
        }

        public fun fromFunction(function: KFunction<*>): DefaultAnnotationBeanDefinition {
            val parameters = function.parameters
            val rawName = function.name
            val name = if (rawName.startsWith("<get-") || rawName.startsWith("<set-"))
                rawName.substring(5, rawName.length - 1)
            else
                rawName

            return DefaultAnnotationBeanDefinition(
                function.returnType.kotlinClass!!,
                function.mergedAnnotations,
                { args ->
                    function.withAccessibility {
                        function.call(*args.toTypedArray())!!
                    }
                },
                parameters,
                name
            )
        }

        public fun fromProperty(property: KProperty<*>): DefaultAnnotationBeanDefinition {
            val parameters = property.valueParameters
            return DefaultAnnotationBeanDefinition(
                property.returnType.kotlinClass!!,
                property.mergedAnnotations,
                { args ->
                    property.withAccessibility {
                        property.call(*args.toTypedArray())!!
                    }
                },
                parameters,
                property.name
            )
        }

        public fun <T: Any> fromProvider(type: KClass<T>, provider: ()->T): DefaultAnnotationBeanDefinition{
            return DefaultAnnotationBeanDefinition(
                type,
                type.mergedAnnotations,
                {provider()},
                emptyList(),
                type.resolveBeanName() ?: throw IllegalArgumentException("Cannot determine default bean name")
            )
        }

        private fun resolveConstructor(type: KClass<*>): KFunction<*> {
            // Constructor with @Inject has highest priority
            val found = type.constructors.filter { it.mergedAnnotations.has<Inject>() }
            if (found.size >= 2)
                throw IllegalArgumentException("Only one constructor can annotate with @Inject")

            if (found.size == 1)
                return found[0]

            // Primary constructor has next priority
            val primary = type.primaryConstructor
            if (primary != null)
                return primary

            // No-arg constructor has next priority
            val noArg = type.constructors.find { it.valueParameters.isEmpty() }
            if (noArg != null)
                return noArg

            // Constructor with argument has last priority
            val constructors = type.constructors
            if (constructors.size != 1)
                throw IllegalArgumentException("Cannot select constructor")

            return constructors.first()
        }
    }


}

private val KType.kotlinClass: KClass<*>?
    get() = classifier as? KClass<*>