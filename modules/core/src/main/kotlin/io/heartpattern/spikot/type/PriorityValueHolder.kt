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

package io.heartpattern.spikot.type

public data class PriorityValue<T>(val priority: Int, val value: T) {
    public operator fun compareTo(other: PriorityValue<*>): Int {
        return priority - other.priority
    }
}

public class PriorityValueHolder<T>(default: T, defaultPriority: Int = Int.MIN_VALUE) {
    private var value: T = default
    private var priority: Int = defaultPriority

    public fun get(): T {
        return value
    }

    public fun set(priorityValue: PriorityValue<T>): Boolean {
        return set(priorityValue.priority, priorityValue.value)
    }

    public fun set(priority: Int, value: T): Boolean {
        return if (this.priority <= priority) {
            this.priority = priority
            this.value = value
            true
        } else {
            false
        }
    }
}