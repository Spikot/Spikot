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

package io.heartpattern.spikot.util

import java.util.*

internal inline fun <T> bfs(initial: T, crossinline propagate: (T) -> Collection<T>): Sequence<T> = sequence {
    yield(initial)

    val queue: Queue<T> = LinkedList()
    queue.add(initial)

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        val append = propagate(current)
        yieldAll(append)
        queue.addAll(append)
    }
}

internal inline fun <T> uniqueBfsAll(initial: Collection<T>, crossinline propagate: (T) -> Collection<T>): Sequence<T> = sequence {
    val uniqueInitial = initial.distinct()

    yieldAll(uniqueInitial)

    val visited = HashSet<T>()
    val queue: Queue<T> = LinkedList()
    visited += initial
    queue.addAll(uniqueInitial)

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        val append = propagate(current)
        for (new in append) {
            if (new !in visited) {
                yield(new)
                visited += new
                queue.add(new)
            }
        }
    }
}

internal inline fun <T> uniqueBfs(initial: T, crossinline propagate: (T) -> Collection<T>): Sequence<T> = uniqueBfsAll(listOf(initial), propagate)