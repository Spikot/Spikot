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

package io.heartpattern.spikot.classpath

import io.heartpattern.spikot.reflection.isAnnotation
import java.util.*
import kotlin.collections.HashSet
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

@Suppress("UNCHECKED_CAST")
public fun Classpath.getTypesMetaAnnotatedWith(
    annotation: KClass<out Annotation>
): Set<KClass<*>> {
    val hierarchy = HierarchyClasspath(this)

    val result = HashSet<KClass<*>>()
    val searchQueue = LinkedList<KClass<out Annotation>>()
    searchQueue.add(annotation)

    while (searchQueue.isNotEmpty()) {
        val current = searchQueue.poll()

        val annotatedSet = hierarchy.getTypesAnnotatedWith(current)
        for (annotated in annotatedSet) {
            if (annotated.isAnnotation && !result.contains(annotated))
                searchQueue.add(annotated as KClass<out Annotation>)

            if (loadClass(annotated.qualifiedName!!) != null)
                result.add(annotated)
        }
    }

    return result
}

@Suppress("UNCHECKED_CAST")
public fun Classpath.getFunctionsMetaAnnotatedWith(
    annotation: KClass<out Annotation>
): Set<KFunction<*>> {
    val result = HashSet<KFunction<*>>()

    val metaAnnotatedTypes = getTypesMetaAnnotatedWith(annotation)
    for (meta in metaAnnotatedTypes) {
        if (!meta.isAnnotation)
            continue

        result.addAll(getFunctionsAnnotatedWith(meta as KClass<out Annotation>))
    }

    return result
}

public inline fun <reified T : Annotation> Classpath.getTypesMetaAnnotatedWith(): Set<KClass<*>> = getTypesMetaAnnotatedWith(T::class)

public inline fun <reified T : Annotation> Classpath.getFunctionsMetaAnnotatedWith(): Set<KFunction<*>> = getFunctionsMetaAnnotatedWith(T::class)