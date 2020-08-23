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

package io.heartpattern.spikot.annotation

import io.heartpattern.spikot.type.fold
import io.heartpattern.spikot.type.tryEither
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaGetter

internal data class AnnotationAttribute private constructor(
    val attribute: KProperty1<Annotation, Any>
) {
    @Suppress("UNCHECKED_CAST")
    val annotation: KClass<out Annotation>
        get() = attribute.instanceParameter!!.type.classifier as KClass<out Annotation>

    val type: KClass<*>
        get() = attribute.returnType.classifier as KClass<*>

    val name: String
        get() = attribute.name

    val rootAlias: AnnotationAttribute = resolveRootAlias(attribute) ?: this


    val default: Any? by lazy {
        attribute.javaGetter!!.defaultValue
    }

    fun get(annotation: Annotation): Any {
        return attribute.invoke(annotation)
    }

    fun getNonDefault(annotation: Annotation): Any? {
        val value = get(annotation)
        return if (value == default)
            null
        else
            value
    }

    private fun resolveRootAlias(attribute: KProperty1<Annotation, *>): AnnotationAttribute? {
        val properties = mutableSetOf(attribute)

        var property = attribute
        while (true) {
            val alias = property.javaGetter!!.getAnnotationsByType(AliasFor::class.java).firstOrNull() ?: break

            val aliasClass = if (alias.annotation == Annotation::class) property.declaringClass
            else alias.annotation

            val aliasAttribute = if (alias.attribute == "") property.name
            else alias.attribute

            @Suppress("UNCHECKED_CAST") val aliasProperty = tryEither {
                aliasClass.memberProperties.find { it.name == aliasAttribute }!! as KProperty1<Annotation, *>
            }.fold(
                ifLeft = {
                    throw AnnotationAttributeNotFoundException(
                        aliasClass as KClass<out Annotation>,
                        aliasAttribute,
                        it
                    )
                },
                ifRight = {
                    it
                }
            )

            if (aliasProperty in properties)
                break

            if (aliasProperty.returnType != property.returnType)
                throw AnnotationConfigurationException(
                    "${formatAttribute(aliasProperty)} and ${formatAttribute(property)} is alias but return type is different"
                )

            if (aliasProperty.declaringClass == property.declaringClass &&
                aliasProperty.javaGetter!!.defaultValue != property.javaGetter!!.defaultValue)
                throw AnnotationConfigurationException(
                    "${formatAttribute(aliasProperty)} and ${formatAttribute(property)} is alias in same annotation but default value is mismatch"
                )

            if (aliasProperty.declaringClass != property.declaringClass &&
                property.declaringClass.annotations.none { it.annotationClass == aliasClass })
                throw AnnotationConfigurationException(
                    "${formatAttribute(aliasProperty)} and ${formatAttribute(property)} is alias but ${aliasProperty.declaringClass.simpleName} does not annotate ${property.declaringClass.simpleName}"
                )

            if (aliasClass != property.declaringClass)
                properties.clear()

            properties += aliasProperty
            property = aliasProperty
        }

        val result = properties.minByOrNull { it.name }!! // Never null

        if (result == attribute)
            return null

        return create(result)
    }

    private fun formatAttribute(property: KProperty<*>): String {
        return "${property.declaringClass.simpleName}.${property.name}"
    }

    companion object {
        private val cache = mutableMapOf<KProperty<*>, AnnotationAttribute>()

        fun create(attribute: KProperty1<Annotation, *>): AnnotationAttribute {
            return cache.getOrPut(attribute) {
                @Suppress("UNCHECKED_CAST")
                AnnotationAttribute(attribute as KProperty1<Annotation, Any>)
            }
        }
    }
}

private val KProperty<*>.declaringClass: KClass<*>
    get() = instanceParameter!!.type.kotlinClass

private val KType.kotlinClass: KClass<*>
    get() = classifier as KClass<*>