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

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

internal class AnnotationAccessor<A : Annotation> private constructor(
    val type: KClass<A>
) {
    private val attributeMap = mutableMapOf<String, AnnotationAttribute>()
    private val aliasMap = mutableMapOf<AnnotationAttribute, MutableList<AnnotationAttribute>>()

    init {
        for (property in type.memberProperties) {
            @Suppress("UNCHECKED_CAST")
            val attribute = AnnotationAttribute.create(property as KProperty1<Annotation, *>)

            val alias = attribute.rootAlias
            aliasMap.getOrPut(alias) { mutableListOf() }.add(attribute)
            attributeMap[property.name] = attribute
        }
    }

    val attributes: Set<String>
        get() = attributeMap.keys

    fun getAttribute(name: String): AnnotationAttribute? {
        return attributeMap[name]
    }

    fun getAttributeValue(annotation: Annotation, attribute: AnnotationAttribute): Any? {
        var lastDefault: Any? = null

        for (alias in aliasMap[attribute.rootAlias] ?: emptyList()) {
            lastDefault = alias.get(annotation)
            if (lastDefault != alias.default)
                return lastDefault
        }

        return lastDefault
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnnotationAccessor<*>) return false

        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }

    override fun toString(): String {
        return "AnnotationAccessor(type=$type)"
    }

    companion object {
        private val cache = mutableMapOf<KClass<*>, AnnotationAccessor<*>>()

        operator fun <A : Annotation> invoke(type: KClass<A>): AnnotationAccessor<A> {
            @Suppress("UNCHECKED_CAST")
            return cache.getOrPut(type) {
                AnnotationAccessor(type)
            } as AnnotationAccessor<A>
        }
    }
}