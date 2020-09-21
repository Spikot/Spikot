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