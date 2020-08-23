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

import io.heartpattern.spikot.util.uniqueBfs
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

public data class HierarchyClasspath(
    val classpath: Classpath
) : Classpath {
    private val deepParent = uniqueBfs(classpath, Classpath::parents).toList()

    override val parents: Collection<Classpath>
        get() = classpath.parents

    override fun loadClass(name: String): KClass<*>? {
        for (parent in deepParent) {
            val loaded = parent.loadClass(name)
            if (loaded != null)
                return loaded
        }

        return null
    }

    override fun getAllTypes(): Collection<String> {
        return merge { it.getAllTypes() }
    }

    override fun getTypesAnnotatedWith(annotation: KClass<out Annotation>): Collection<KClass<*>> {
        return merge { it.getTypesAnnotatedWith(annotation) }
    }

    override fun getFunctionsAnnotatedWith(annotation: KClass<out Annotation>): Collection<KFunction<*>> {
        return merge { it.getFunctionsAnnotatedWith(annotation) }
    }

    override fun <T : Any> getSubTypesOf(superType: KClass<T>): Collection<KClass<out T>> {
        return merge { it.getSubTypesOf(superType) }
    }

    private inline fun <T> merge(func: (Classpath) -> Collection<T>): Collection<T> {
        val result = HashSet<T>()

        for (parent in deepParent) {
            result.addAll(func(parent))
        }

        return result
    }
}