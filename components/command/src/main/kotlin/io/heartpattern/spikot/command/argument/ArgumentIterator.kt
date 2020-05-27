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

package io.heartpattern.spikot.command.argument

class ArgumentIterator(
    val arguments: List<String>,
    _pointer: Int = 0
) : ListIterator<String> {
    var pointer: Int = _pointer
        internal set

    override fun hasPrevious(): Boolean = pointer > 0
    override fun hasNext(): Boolean = pointer < arguments.size

    override fun next(): String {
        if (!hasNext())
            throw NoSuchElementException("No more element in CommandArgumentIterator. pointer: $pointer")

        return arguments[pointer++]
    }

    override fun previous(): String {
        if (!hasPrevious())
            throw NoSuchElementException("No more element in CommandArgumentIterator. Pointer: $pointer")

        return arguments[--pointer]
    }

    fun nextOrNull(): String? {
        return if (hasNext()) next() else null
    }

    fun previousOrNull(): String? {
        return if (hasPrevious()) previous() else null
    }

    override fun nextIndex(): Int {
        return pointer
    }

    override fun previousIndex(): Int {
        return pointer - 1
    }
}