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

package io.heartpattern.spikot.scope

import io.heartpattern.spikot.SpikotPlugin
import io.heartpattern.spikot.bean.*
import io.heartpattern.spikot.bean.definition.BeanDefinition
import io.heartpattern.spikot.bean.definition.BeanDefinitionRegistry
import io.heartpattern.spikot.condition.ConditionContext
import io.heartpattern.spikot.extension.catchAll
import io.heartpattern.spikot.type.TypedMap
import mu.KotlinLogging
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.reflect.full.isSubclassOf

private val logger = KotlinLogging.logger {}

public class BeanDefinitionRegistryScopeInstance constructor(
    public override val scope: String,
    public override val name: String,
    public override val owingPlugin: SpikotPlugin,
    beanDefinitionRegistry: BeanDefinitionRegistry,
    public override val parents: Collection<ScopeInstance>,
    contextualObjects: Map<String, Any>
) : ScopeInstance {
    private val beans = HashMap<String, BeanHolder>()

    private val beanLoadOrder = LinkedList<BeanHolder>()

    private val children = HashSet<ScopeInstance>()

    private val beanInCreation = HashSet<BeanHolder>()

    private val contextualObjects: Map<String, Any> = contextualObjects + mapOf(
        "plugin" to owingPlugin,
        "scope" to this
    )

    override val allBeanProcessors: Collection<String>
        get() = (parents.flatMap { it.allBeanProcessors } +
            beans.values.filter { it.definition.type.isSubclassOf(BeanProcessor::class) }.map { it.definition.name })
            .toSet()

    private val beanProcessors = LinkedList<BeanProcessor>()

    override var isClosed: Boolean = false
        private set

    override val allBeanName: Collection<String>
        get() = parents.flatMap { it.allBeanName } + beans.keys

    override val data: TypedMap = TypedMap()

    init {
        val conditionContext = ConditionContext(this)

        for (definition in beanDefinitionRegistry.getAllBeanDefinition(scope)) {
            if (!definition.checkCondition(conditionContext)) {
                logger.trace { "Condition false: $definition" }
                continue
            }

            if (beans.contains(definition.name))
                logger.error { "Bean ${definition.name} is already registered in scope" }

            val holder = BeanHolder(definition)
            beans[definition.name] = holder
        }

        for (parent in parents) {
            if (parent.isClosed)
                throw IllegalStateException("$parent is already closed")
            parent.registerChildScope(this)
        }
    }

    public fun preInitializeBeanProcessor() {
        if (beanProcessors.isNotEmpty()) // Prevent executing twice or more
            return

        beans.values.sortedBy { it.definition.loadOrder }.forEach {
            if (!it.definition.type.isSubclassOf(BeanProcessor::class))
                return@forEach

            logger.trace { "Find bean processor: ${it.definition}" }

            logger.catchAll("Exception thrown while initializing bean processor ${it.definition}") {
                getOrCreateBean(it.definition.description, it) as BeanProcessor
            }
        }

        for (bp in allBeanProcessors) {
            beanProcessors += getBean<BeanProcessor>(bp)
        }
    }

    public fun preInitializeBeans() {
        preInitializeBeanProcessor() // Ensure bean processor initialized
        beans.values.sortedBy { it.definition.loadOrder }.forEach {
            logger.catchAll("Exception thrown while eager initializing bean ${it.definition}") {
                if (!it.definition.isLazy)
                    getOrCreateBean(it.definition.description, it)
            }
        }
    }

    public override fun getBean(description: BeanDescription): Any {
        return getContextualObject(description)
            ?: getOrCreateBean(description)
            ?: throw BeanNotFoundException(description, this)
    }

    public override fun getBeans(description: BeanDescription): List<Any> {
        if (description.name != null) // Name is unique
            return listOf(getBean(description))

        val target = ArrayList<BeanHolder>()

        val type = description.type!!

        for ((_, holder) in beans) {
            if (holder.definition.type.isSubclassOf(type))
                target += holder
        }

        target.sortWith { a, b ->
            when {
                a.definition.isPrimary && !b.definition.isPrimary -> 1
                !a.definition.isPrimary && b.definition.isPrimary -> -1
                else -> a.definition.loadOrder - b.definition.loadOrder
            }
        }

        return getContextualObjects(description) + target.map { getOrCreateBean(description, it) }
    }

    override fun hasBean(description: BeanDescription): Boolean {
        if (getContextualObjects(description).isNotEmpty())
            return true

        if (getBeanHolder(description) != null)
            return true

        for (parent in parents) {
            if (parent.hasBean(description))
                return true
        }

        return false
    }

    override fun hasInitializedBean(description: BeanDescription): Boolean {
        if (getContextualObjects(description).isNotEmpty())
            return true

        val holder = getBeanHolder(description)
        if (holder != null)
            return holder.instance != null

        for (parent in parents) {
            if (parent.hasInitializedBean(description)) {
                return true
            }
        }

        return false
    }

    override fun registerChildScope(scope: ScopeInstance) {
        children.add(scope)
    }

    override fun unregisterChildScope(scope: ScopeInstance) {
        children.remove(scope)
    }

    public override fun close() {
        if (isClosed)
            return
        isClosed = true

        for (child in children.toList())
            child.close()

        for (holder in beanLoadOrder.descendingIterator()) {
            destroyBean(holder)
        }

        for (parent in parents) {
            parent.unregisterChildScope(this)
        }
    }

    private fun getContextualObject(description: BeanDescription): Any? {
        val set = getContextualObjects(description)
        return when {
            set.isEmpty() -> null
            set.size == 1 -> set.first()
            else -> throw IllegalArgumentException("Cannot determine unique bean for $description")
        }
    }

    private fun getContextualObjects(description: BeanDescription): Collection<Any> {
        val named = contextualObjects[description.getDeterminedBeanName()]
        if (named != null) {
            return if (description.type != null && description.type.isInstance(named))
                setOf(named)
            else
                emptySet()
        }

        if (description.name != null)
            return emptySet()

        val type = description.type

        if (type != null) {
            return contextualObjects.values.filter { type.isInstance(it) }
        }

        return emptySet()
    }

    private fun getOrCreateBean(description: BeanDescription): Any? {
        val beanHolder = getBeanHolder(description)

        if (beanHolder != null)
            return getOrCreateBean(description, beanHolder)

        for (parent in this.parents) {
            if (parent.hasBean(description)) {
                return parent.getBean(description)
            }
        }

        return null
    }

    private fun getOrCreateBean(description: BeanDescription, holder: BeanHolder): Any {
        if (holder.instance == null && holder.error == null) {
            createBean(holder)
        }

        return holder.instance ?: throw BeanCreationException(description, holder.error!!)
    }

    private fun getBeanHolder(description: BeanDescription): BeanHolder? {
        val name = description.getDeterminedBeanName()
        val type = description.type

        val nameBean = beans[name]
        if (nameBean != null) {
            if (type != null && !nameBean.definition.type.isSubclassOf(type))
                return null

            return nameBean
        }

        // If bean name is explicitly set, do not search for type
        if (description.name != null)
            return null

        var typeBean: BeanHolder? = null
        if (type != null) {
            for (bean in beans.values) {
                if (bean.definition.type.isSubclassOf(type)) {
                    typeBean = if (typeBean == null)
                        bean
                    else if (!typeBean.definition.isPrimary && bean.definition.isPrimary)
                        bean
                    else
                        throw IllegalArgumentException("Cannot select unique bean for $description. Candidate is ${typeBean.definition} and ${bean.definition}")
                }
            }
        }

        return typeBean
    }

    private fun createBean(holder: BeanHolder) {
        try {
            logger.trace { "Create bean ${holder.definition.description}" }
            if (holder in beanInCreation)
                throw IllegalArgumentException("Recursive bean dependency found ${holder.definition}")

            beanInCreation += holder
            val instance = holder.definition.create(this)
            holder.instance = instance
            beanInCreation -= holder

            beanLoadOrder.add(holder)

            val context = BeanProcessorContext(this)

            if (instance !is BeanProcessor) { // Do not process bean processor.
                for (processor in beanProcessors) {
                    processor.beforeInitialize(context, holder.definition, instance)
                }
            }
            holder.definition.invokeInitializeFunction(instance)
            holder.destroyCallback.add {
                holder.definition.invokeDestroyFunction(it)
                if (instance !is BeanProcessor) {
                    for (processor in beanProcessors.descendingIterator()) {
                        processor.afterDestroy(context, holder.definition, instance)
                    }
                }
            }
        } catch (e: Exception) {
            holder.instance = null
            holder.error = e
        }
    }

    private fun destroyBean(holder: BeanHolder) {
        val instance = holder.instance
            ?: throw IllegalStateException("Bean is not initialized or already destroyed")

        for (callback in holder.destroyCallback) {
            logger.catchAll("Exception thrown while executing destroy callback") {
                callback(instance)
            }
        }
        beans.remove(holder.definition.name, holder)
        holder.instance = null
        holder.error = IllegalStateException("Destroyed bean")
    }

    override fun toString(): String {
        return "BDRScopeInstance(scope='$scope', name='$name', plugin='${owingPlugin.name}', isClosed=$isClosed)"
    }
}

// Do not implement equals and hashcode. Reference check is enough.
private class BeanHolder(
    val definition: BeanDefinition,
    var instance: Any? = null,
    var error: Exception? = null,
    val destroyCallback: LinkedList<(Any) -> Unit> = LinkedList()
) {
    override fun toString(): String {
        return "BeanHolder(definition=$definition, instance=$instance, error=$error, destroyCallback=$destroyCallback)"
    }
}