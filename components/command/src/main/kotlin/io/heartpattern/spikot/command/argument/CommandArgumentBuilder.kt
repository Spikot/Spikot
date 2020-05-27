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

import io.heartpattern.spikot.chat.FancyChat
import io.heartpattern.spikot.chat.chat
import io.heartpattern.spikot.command.CommandContext
import io.heartpattern.spikot.type.*
import java.util.*

typealias ArgumentModifier<S, T> = (context: CommandContext, source: S) -> Either<Any, T>
typealias ArgumentCompleter = (context: CommandContext, index: Int, hint: String) -> List<String>

@Suppress("UNCHECKED_CAST")
open class CommandArgumentBuilder<out T> internal constructor() : CommandArgument<T> {
    private val nameHolder = PriorityValueHolder<FancyChat>(chat("string"))
    private val descriptionHolder = PriorityValueHolder<FancyChat>(chat("string argument"))
    private val completerHolder = PriorityValueHolder<ArgumentCompleter>({ _, _, _ -> emptyList() })
    private val modifiers = LinkedList<ArgumentModifier<Any?, Any?>>()

    override val name: FancyChat
        get() = nameHolder.get()

    override val description: FancyChat
        get() = descriptionHolder.get()

    fun setName(priority: Int, name: FancyChat): CommandArgumentBuilder<T> = apply {
        this.nameHolder.set(priority, name)
    }

    fun setDescription(priority: Int, description: FancyChat): CommandArgumentBuilder<T> = apply {
        this.descriptionHolder.set(priority, description)
    }

    fun setCompleter(priority: Int, completer: ArgumentCompleter): CommandArgumentBuilder<T> = apply {
        this.completerHolder.set(priority, completer)
    }

    fun name(name: String): CommandArgumentBuilder<T> = apply {
        this.nameHolder.set(Int.MAX_VALUE, chat(name))
    }

    fun name(chat: FancyChat): CommandArgumentBuilder<T> = apply {
        this.nameHolder.set(Int.MAX_VALUE, chat)
    }

    fun description(description: String): CommandArgumentBuilder<T> = apply {
        this.descriptionHolder.set(Int.MAX_VALUE, chat(description))
    }

    fun description(description: FancyChat): CommandArgumentBuilder<T> = apply {
        this.descriptionHolder.set(Int.MAX_VALUE, description)
    }

    fun <R> modify(modifier: ArgumentModifier<T, R>): CommandArgumentBuilder<R> {
        modifiers += modifier as ArgumentModifier<Any?, Any?>
        return this as CommandArgumentBuilder<R>
    }

    fun completer(completer: ArgumentCompleter): CommandArgumentBuilder<T> = apply {
        this.completerHolder.set(Int.MAX_VALUE, completer)
    }

    override fun parse(context: CommandContext, argument: ArgumentIterator): Either<Any, T> {
        var intermediate: Any? = argument
        for (modifier in modifiers) {
            intermediate = modifier(context, intermediate).fold(
                ifLeft = { return Left(it) },
                ifRight = { it }
            )
        }

        return Right(intermediate as T)
    }

    override fun complete(context: CommandContext, index: Int, last: String): List<String> {
        return completerHolder.get().invoke(context, index, last)
    }
}