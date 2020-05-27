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

package io.heartpattern.spikot.command

import io.heartpattern.spikot.component.Component

abstract class CommandComponent(
    private vararg val names: String,
    private val configure: CommandDSL.() -> Unit
) : Component() {
    private lateinit var command: SpikotCommand

    final override fun onEnable() {
        command = plugin.registerCommand(name = *names, configure = configure)
    }

    final override fun onDisable() {
        CommandAdapter.unregisterCommand(command)
    }
}