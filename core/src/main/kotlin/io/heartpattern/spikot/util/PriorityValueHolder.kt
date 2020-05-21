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

package io.heartpattern.spikot.util

data class PriorityValue<T>(val priority: Int, val value: T) {
    operator fun compareTo(other: PriorityValue<*>): Int {
        return priority - other.priority
    }
}

class PriorityValueHolder<T>(default: T, defaultPriority: Int = Int.MIN_VALUE) {
    private var value: T = default
    private var priority: Int = defaultPriority

    fun get(): T {
        return value
    }

    fun set(priorityValue: PriorityValue<T>): Boolean {
        return set(priorityValue.priority, priorityValue.value)
    }

    fun set(priority: Int, value: T): Boolean {
        return if (this.priority <= priority) {
            this.priority = priority
            this.value = value
            true
        } else {
            false
        }
    }
}