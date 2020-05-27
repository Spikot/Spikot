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

@file:Suppress("UNCHECKED_CAST")

package io.heartpattern.spikot.command

import io.heartpattern.spikot.command.argument.CommandArgument

fun CommandDSL.execute(handler: CommandContext.() -> Unit) {
    executes {
        handler()
    }
}

fun <T1> CommandDSL.execute(arg1: CommandArgument<T1>, handler: CommandContext.(T1) -> Unit) {
    executes(arg1) { list ->
        handler(list[0] as T1)
    }
}

fun <T1, T2> CommandDSL.execute(arg1: CommandArgument<T1>, arg2: CommandArgument<T2>, handler: CommandContext.(T1, T2) -> Unit) {
    executes(arg1, arg2) { list ->
        handler(list[0] as T1, list[1] as T2)
    }
}