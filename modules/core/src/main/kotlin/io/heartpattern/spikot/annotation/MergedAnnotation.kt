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

import java.lang.reflect.Proxy
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

/**
 * Represent merged information of meta annotation.
 */
public class MergedAnnotation<A : Annotation> internal constructor(
    public val rawAnnotation: A,
    public val distance: Int,
    public val source: KAnnotatedElement,
    public val metaSource: MergedAnnotation<*>?
) {
    /**
     * Type of annotation that this class hold
     */
    @Suppress("UNCHECKED_CAST")
    public val type: KClass<A> = rawAnnotation.annotationClass as KClass<A>

    /**
     * Get root of meta annotation.
     */
    public val root: MergedAnnotation<*> = run {
        var r: MergedAnnotation<*> = this@MergedAnnotation
        while (r.metaSource != null)
            r = r.metaSource!!
        r
    }

    /**
     * Determine if annotation is directly presented.
     */
    public val isDirectlyPresent: Boolean
        get() = distance == 0

    /**
     * Determine if annotation is meta presented.
     */
    public val isMetaPresent: Boolean
        get() = distance > 0

    /**
     * List of all annotation from this to root.
     */
    public val metaAnnotationList: List<MergedAnnotation<*>> by lazy {
        val list = arrayListOf<MergedAnnotation<*>>()
        var r: MergedAnnotation<*>? = this@MergedAnnotation
        while (r != null) {
            list += r
            r = r.metaSource
        }
        list
    }

    internal val annotationAccessor: AnnotationAccessor<A> = AnnotationAccessor(type)

    public val attributes: Set<String>
        get() = annotationAccessor.attributes

    /**
     * Get attribute value of [name] considering [AliasFor]
     * Throw [AnnotationAttributeNotFoundException] if such attribute does not exist in annotation.
     */
    public fun getAttribute(name: String): Any {
        return getAttribute(
            annotationAccessor.getAttribute(name)
                ?: throw AnnotationAttributeNotFoundException(type, name)
        ) ?: throw AnnotationAttributeNotFoundException(type, name)
    }

    /**
     * Get attribute value of [name] with [type] considering [AliasFor]
     * Throw [AnnotationAttributeNotFoundException] if such attribute does not exist in annotation.
     */
    public fun <T : Any> getTypedAttribute(name: String, type: KClass<T>): T {
        return type.cast(getAttribute(name))
    }

    /**
     * Get attribute value of [name] with type [T] considering [AliasFor]
     * Throw [AnnotationAttributeNotFoundException] if such attribute does not exist in annotation.
     */
    public inline fun <reified T : Any> getTypedAttribute(name: String): T {
        return getTypedAttribute(name, T::class)
    }

    /**
     * Return merged annotation instance
     */
    @Suppress("UNCHECKED_CAST")
    public fun synthesize(): A {
        return Proxy.newProxyInstance(
            this::class.java.classLoader,
            arrayOf(type.java),
            MergedAnnotationInvocationHandler(this)
        ) as A
    }

    private fun getAttribute(attribute: AnnotationAttribute): Any? {
        return metaSource?.getAttribute(attribute)
            ?: annotationAccessor.getAttributeValue(
                rawAnnotation,
                attribute
            )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MergedAnnotation<*>) return false

        if (metaAnnotationList != other.metaAnnotationList) return false

        return true
    }

    override fun hashCode(): Int {
        return metaAnnotationList.hashCode()
    }

    override fun toString(): String {
        return "MergedAnnotation(annotation=$rawAnnotation, type=$type, distance=$distance, source=$source)"
    }
}