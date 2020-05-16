/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package kr.heartpattern.spikot.reflection.annotation

import kr.heartpattern.spikot.reflection.withAccessibility
import java.util.*
import kotlin.collections.HashSet
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

data class MetaAnnotation<T : Annotation>(
    val annotation: T,
    val parent: MetaAnnotation<*>?,
    val depth: Int
) {
    /**
     * Get value of field named [attribute]. This function support [AliasFor] annotation.
     */
    fun getAttribute(attribute: String): Any {
        return getAttribute(annotation.annotationClass, attribute)!!

    }

    private fun getAttribute(annotation: KClass<out Annotation>, attribute: String): Any? {
        return parent?.getAttribute(annotation, attribute) ?: run {
            if (this.annotation.annotationClass == annotation) {
                val method = this.annotation.javaClass.getDeclaredMethod(attribute)
                method.withAccessibility {
                    method.invoke(this.annotation)
                }
            } else {
                for (method in this.annotation.annotationClass.java.declaredMethods) {
                    val alias = method.getAnnotation(AliasFor::class.java) ?: continue

                    if (alias.annotation == annotation && alias.attribute == attribute) {
                        val realMethod = this.annotation.javaClass.getDeclaredMethod(method.name)
                        return@run realMethod.withAccessibility {
                            realMethod.invoke(this.annotation)
                        }
                    }
                }
                return@run null
            }
        }
    }
}

/**
 * Find all meta annotated [T] annotating [this].
 * This class find annotation recursively, which means [T] annotating some annotation which annotate [this] is also found by this method.
 * Sequence of found annotation is sorted in depth.
 * Each annotation is checked once, therefore if annotation is found multiple time, only the most shallow thing is handled.
 */
inline fun <reified T : Annotation> KClass<*>.findMetaAnnotations(): List<MetaAnnotation<T>> {
    return findMetaAnnotations(T::class)
}

/**
 * Find all meta annotated [annotationType] annotating [this].
 * This class find annotation recursively, which means [T] annotating some annotation which annotate [this] is also found by this method.
 * Sequence of found annotation is sorted in depth.
 * Each annotation is checked once, therefore if annotation is found multiple time, only the most shallow thing is handled.
 */
fun <T : Annotation> KClass<*>.findMetaAnnotations(annotationType: KClass<T>): List<MetaAnnotation<T>> {
    val searchQueue = LinkedList<MetaAnnotation<*>>()
    val visited = HashSet<String>()
    val result = LinkedList<MetaAnnotation<T>>()

    for (annotation in annotations)
        searchQueue += MetaAnnotation(annotation, null, 0)

    while (searchQueue.isNotEmpty()) {
        val metaAnnotation = searchQueue.pollFirst()
        val (annotation, _, depth) = metaAnnotation
        if (annotation.annotationClass == annotationType) {
            @Suppress("UNCHECKED_CAST")
            result += metaAnnotation as MetaAnnotation<T>
        } else if (annotation.annotationClass.jvmName !in visited) {
            visited += annotation.annotationClass.jvmName
            for (meta in annotation.annotationClass.annotations)
                if (meta.annotationClass.jvmName !in visited)
                    searchQueue += MetaAnnotation(meta, metaAnnotation, depth + 1)
        }
    }

    return result
}

/**
 * Find first meta annotated [T] annotating [this].
 * This class find annotation recursively, which means [T] annotating some annotation which annotate [this] is also found by this method.
 * Returning annotation is the most shallow depth annotation.
 */
inline fun <reified T : Annotation> KClass<*>.findMetaAnnotation(): MetaAnnotation<T>? {
    return findMetaAnnotation(T::class)
}

/**
 * Find first meta annotated [annotationType] annotating [this].
 * This class find annotation recursively, which means [T] annotating some annotation which annotate [this] is also found by this method.
 * Returning annotation is the most shallow depth annotation.
 */
fun <T : Annotation> KClass<*>.findMetaAnnotation(annotationType: KClass<T>): MetaAnnotation<T>? {
    val searchQueue = LinkedList<MetaAnnotation<*>>()
    val visited = HashSet<String>()

    for (annotation in annotations)
        searchQueue += MetaAnnotation(annotation, null, 0)

    while (searchQueue.isNotEmpty()) {
        val metaAnnotation = searchQueue.pollFirst()
        val (annotation, _, depth) = metaAnnotation
        if (annotation.annotationClass == annotationType) {
            @Suppress("UNCHECKED_CAST")
            return metaAnnotation as MetaAnnotation<T>
        } else if (annotation.annotationClass.jvmName !in visited) {
            visited += annotation.annotationClass.jvmName
            for (meta in annotation.annotationClass.annotations)
                if (meta.annotationClass.jvmName !in visited)
                    searchQueue += MetaAnnotation(meta, metaAnnotation, depth + 1)
        }
    }

    return null
}