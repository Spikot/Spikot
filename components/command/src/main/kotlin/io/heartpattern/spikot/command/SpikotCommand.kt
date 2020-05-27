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

import io.heartpattern.spikot.SpikotPlugin
import io.heartpattern.spikot.command.argument.ArgumentIterator
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class SpikotCommand(
    val plugin: SpikotPlugin,
    private val node: InternalCommandNode
) : Command(node.names[0]) {
    private val alias = node.names.drop(1).toMutableList()

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        node.execute(
            CommandContext(this, sender, label, args.toList()),
            ArgumentIterator(listOf(commandLabel, *args)),
            true
        )

        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        val completed = node.tabComplete(
            CommandContext(this, sender, alias, args.toList()),
            ArgumentIterator(listOf(alias, *args)),
            args.last(),
            args.size - 1
        )

        val list = ArrayList<String>(completed.size)
        completed.forEach {
            if (it.startsWith(args.last()))
                list.add(it)
        }

        return list
    }

    override fun getAliases(): MutableList<String> {
        return alias
    }
}