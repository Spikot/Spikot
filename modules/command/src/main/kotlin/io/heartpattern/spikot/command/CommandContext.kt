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

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

open class CommandContext(
    val sender: CommandSender,
    val command: Command,
    val label: String,
    val arguments: List<String>
) {
    operator fun component1(): CommandSender = sender
    operator fun component2(): Command = command
    operator fun component3(): String = label
    operator fun component4(): List<String> = arguments

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommandContext

        if (sender != other.sender) return false
        if (command != other.command) return false
        if (label != other.label) return false
        if (arguments != other.arguments) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sender.hashCode()
        result = 31 * result + command.hashCode()
        result = 31 * result + label.hashCode()
        result = 31 * result + arguments.hashCode()
        return result
    }

    override fun toString(): String {
        return "CommandContext(sender=$sender, command=$command, label='$label', arguments=$arguments)"
    }
}

open class CommandCompletionContext(
    sender: CommandSender,
    command: Command,
    label: String,
    arguments: List<String>,
    val hint: String
) : CommandContext(
    sender,
    command,
    label,
    arguments
) {
    operator fun component5(): String = hint
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CommandCompletionContext) return false
        if (!super.equals(other)) return false

        if (hint != other.hint) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + hint.hashCode()
        return result
    }

    override fun toString(): String {
        return "CommandCompletionContext(sender=$sender, command=$command, label='$label', arguments=$arguments)"
    }
}