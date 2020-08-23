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

import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass

/**
 * Manage all annotation and meta annotation as [MergedAnnotation]
 */
public class MergedAnnotations internal constructor(
    /**
     * Source that all [MergedAnnotation] annotate
     */
    public val source: KAnnotatedElement
) {
    private val mergedAnnotations: List<MergedAnnotation<*>>
    private val ignoreAnnotation = setOf(
        "java.lang.annotation.Retention",
        "java.lang.annotation.Target",
        "java.lang.annotation.Documented",
        "java.lang.annotation.Inherited",
        "java.lang.annotation.Repeatable",
        "kotlin.annotation.Retention",
        "kotlin.annotation.Target",
        "kotlin.annotation.Repeatable",
        "kotlin.annotation.MustBeDocumented"
    )

    init {
        fun filterAnnotation(annotation: Annotation): Boolean {
            val name = annotation.annotationClass.qualifiedName ?: return true
            return name !in ignoreAnnotation
        }

        val annotations = LinkedList<MergedAnnotation<*>>()
        val searchQueue = LinkedList<AnnotationNode>()

        for (annotation in source.annotations)
            searchQueue.add(AnnotationNode(annotation, null, 0))


        while (searchQueue.isNotEmpty()) {
            val (annotation, parent, distance) = searchQueue.pollFirst()

            if (parent?.metaAnnotationList?.any { it.type == annotation.annotationClass } == true)
                throw AnnotationConfigurationException("MetaAnnotation loop is found: $annotation")

            val merge = MergedAnnotation(annotation, distance, source, parent)
            annotations.add(merge)

            for (meta in annotation.annotationClass.annotations)
                if (filterAnnotation(meta))
                    searchQueue.add(AnnotationNode(meta, merge, distance + 1))
        }

        mergedAnnotations = annotations
    }

    /**
     * Get sequence of all declared annotation as [MergedAnnotation]
     */
    public fun asSequence(): Sequence<MergedAnnotation<*>> = mergedAnnotations.asSequence()

    /**
     * Get nearest declared annotation of type [type] as [MergedAnnotation]
     */
    @Suppress("UNCHECKED_CAST")
    public fun <T : Annotation> get(type: KClass<T>): MergedAnnotation<T>? = asSequence().firstOrNull { it.type == type } as MergedAnnotation<T>?

    /**
     * Get nearest declared annotation of type [T] as [MergedAnnotation]
     */
    public inline fun <reified T : Annotation> get(): MergedAnnotation<T>? = get(T::class)

    /**
     * Get all annotation of type [type] sorted by distance as [MergedAnnotation]
     */
    @Suppress("UNCHECKED_CAST")
    public fun <T : Annotation> getAll(type: KClass<T>): List<MergedAnnotation<T>> = asSequence().filter { it.type == type }.toList() as List<MergedAnnotation<T>>

    /**
     * Get all annotation of type [T] sorted by distance as [MergedAnnotation]
     */
    public inline fun <reified T : Annotation> getAll(): List<MergedAnnotation<T>> = getAll(T::class)

    public fun has(type: KClass<out Annotation>): Boolean = get(type) != null
    public inline fun <reified T : Annotation> has(): Boolean = get<T>() != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MergedAnnotations) return false

        if (source != other.source) return false

        return true
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        return result
    }

    override fun toString(): String {
        return "MergedAnnotations(source=$source)"
    }

    private data class AnnotationNode(
        val annotation: Annotation,
        val parent: MergedAnnotation<*>?,
        val distance: Int
    )
}

// Currently only cache MergedAnnotations with default filter
private val mergedAnnotationCache = LinkedHashMap<KAnnotatedElement, MergedAnnotations>()

/**
 * Get [MergedAnnotations] annotating [KAnnotatedElement]
 */
public val KAnnotatedElement.mergedAnnotations: MergedAnnotations
    get() = mergedAnnotationCache.getOrPut(this) { MergedAnnotations(this) }