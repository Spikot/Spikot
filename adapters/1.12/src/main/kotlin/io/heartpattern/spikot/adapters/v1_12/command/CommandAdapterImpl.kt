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

package io.heartpattern.spikot.adapters.v1_12.command

import io.heartpattern.spikot.adapter.Adapter
import io.heartpattern.spikot.command.CommandAdapter
import io.heartpattern.spikot.component.Component
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.craftbukkit.v1_12_R1.CraftServer
import org.bukkit.plugin.Plugin

@Adapter(adapterOf = CommandAdapter::class, version = "1.12.2")
object CommandAdapterImpl : Component(), CommandAdapter {
    private val commandMap = (Bukkit.getServer() as CraftServer).commandMap

    override fun registerCommand(plugin: Plugin, command: Command) {
        commandMap.register(plugin.name, command)
    }

    override fun unregisterCommand(command: Command) {
        command.unregister(commandMap)
    }
}