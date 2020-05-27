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

package io.heartpattern.spikot.test.command

import io.heartpattern.spikot.chat.chat
import io.heartpattern.spikot.command.execute
import io.heartpattern.spikot.command.predef.remain
import io.heartpattern.spikot.command.predef.single
import io.heartpattern.spikot.command.registerCommand
import io.heartpattern.spikot.component.Component
import io.heartpattern.spikot.component.scopes.server.ServerScope

@ServerScope
class CommandTestComponent : Component() {
    override fun onEnable() {
        plugin.registerCommand("test") {
            description = chat("Test description")
            child("sub", "s", "child") {
                description = chat.blue("First child command")
                execute(
                    single.name("first").description("First description")
                        .completer { _, _, _ -> listOf("a", "b") }
                ) { first ->
                    sender.sendMessage("1: $first")
                }

                execute(
                    single.name("first").description("First description")
                        .completer { _, _, _ -> listOf("a", "b", "c", "d") },
                    single.name("second").description("Second description")
                ) { first, second ->
                    sender.sendMessage("1: $first, 2: $second")
                }
            }

            child("no") {
                description = chat.red("No arg child command")
                execute {
                    sender.sendMessage("No arg")
                }
            }

            child("remain") {
                description = chat.red("Remain child command")
                execute(remain) { t ->
                    sender.sendMessage("Remain: " + t.joinToString(","))
                }
            }
        }
    }
}