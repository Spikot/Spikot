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

package io.heartpattern.spikot.command.arguments

import io.heartpattern.spikot.command.CommandDSL
import io.heartpattern.spikot.command.argument.ArgumentIterator
import io.heartpattern.spikot.command.argument.CommandArgumentBuilder
import io.heartpattern.spikot.command.failure.TooFewArgumentCommandFailure
import io.heartpattern.spikot.type.Left
import io.heartpattern.spikot.type.right

val CommandDSL.single: CommandArgumentBuilder<String>
    get() = CommandArgumentBuilder<ArgumentIterator>().modify { _, iter ->
        iter.nextOrNull()?.right() ?: Left(TooFewArgumentCommandFailure(iter.pointer))
    }

fun CommandDSL.multiple(length: Int): CommandArgumentBuilder<List<String>> {
    return CommandArgumentBuilder<ArgumentIterator>().modify { _, iter ->
        val result = ArrayList<String>(length)
        repeat(length) {
            val next = iter.nextOrNull()
                ?: return@modify Left(TooFewArgumentCommandFailure(iter.pointer))
            result.add(next)
        }
        result.right()
    }
}

val CommandDSL.remain: CommandArgumentBuilder<List<String>>
    get() = CommandArgumentBuilder<ArgumentIterator>().modify { _, iter ->
        val result = ArrayList<String>()
        while (iter.hasNext())
            result.add(iter.next())

        result.right()
    }