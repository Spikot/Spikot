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

import io.heartpattern.spikot.chat.FancyChat
import io.heartpattern.spikot.command.argument.ArgumentIterator
import io.heartpattern.spikot.command.argument.CommandArgument
import io.heartpattern.spikot.command.failure.*
import io.heartpattern.spikot.extension.catchAll
import io.heartpattern.spikot.type.isLeft
import mu.KotlinLogging
import org.bukkit.permissions.Permission
import java.util.*
import kotlin.reflect.KClass

private val logger = KotlinLogging.logger {}

sealed class CommandNode {
    abstract val parent: InternalCommandNode?
    abstract val permission: Permission?
    abstract val priority: Int
    abstract fun execute(context: CommandContext, arguments: ArgumentIterator, printError: Boolean): Boolean
    abstract fun tabComplete(context: CommandContext, arguments: ArgumentIterator, last: String, index: Int): List<String>
    abstract fun handleFailure(context: CommandContext, node: CommandNode, failure: Any)
}

class LeafCommandNode(
    override val parent: InternalCommandNode,
    override val permission: Permission? = null,
    override val priority: Int,
    val arguments: List<CommandArgument<Any?>>,
    val handler: CommandExecutor
) : CommandNode() {
    override fun execute(context: CommandContext, arguments: ArgumentIterator, printError: Boolean): Boolean {
        val parsed = LinkedList<Any?>()
        val pointer = arguments.pointer

        for (arg in this.arguments) {
            val result = arg.parse(context, arguments)
            if (result.isLeft()) {
                if (printError)
                    handleFailure(context, this, result.left!!)

                return false
            } else {
                parsed.add(result.right)
            }
        }

        if (arguments.hasNext()) {
            if (printError)
                handleFailure(context, this, TooManyArgumentFailure(arguments.pointer, context.arguments.size))

            arguments.pointer = pointer
            return false
        }


        try {
            handler(context, parsed)
        } catch (e: Exception) {
            handleFailure(context, this, e)
        }

        return true
    }

    override fun tabComplete(context: CommandContext, arguments: ArgumentIterator, last: String, index: Int): List<String> {
        if (this.arguments.size <= index - arguments.pointer + 1)
            return emptyList()

        return this.arguments[index - arguments.pointer + 1].complete(context, index, last)
    }

    override fun handleFailure(context: CommandContext, node: CommandNode, failure: Any) {
        parent.handleFailure(context, node, failure)
    }
}

class InternalCommandNode internal constructor(
    override val parent: InternalCommandNode?,
    val names: List<String>,
    val description: FancyChat,
    override val permission: Permission? = null,
    override val priority: Int,
    val children: List<CommandNode>,
    private val failureHandlers: HashMap<KClass<*>, FailureHandler<Any>>
) : CommandNode() {
    override fun execute(context: CommandContext, arguments: ArgumentIterator, printError: Boolean): Boolean {
        if (!arguments.hasNext())
            return false
        if (arguments.next().toLowerCase() !in names) {
            arguments.previous()
            return false
        }

        for (child in children) {
            if (child.execute(context, arguments, child === children.last()))
                return true
        }

        handleFailure(context, this, NoSuchCommandFailure)

        return true
    }

    override fun tabComplete(context: CommandContext, arguments: ArgumentIterator, last: String, index: Int): List<String> {
        if (!arguments.hasNext())
            return emptyList()

        val next = arguments.next()

        return if (!arguments.hasNext()) {
            names.toList()
        } else if (next in names) {
            children.flatMap {
                val pointer = arguments.pointer
                val result = it.tabComplete(context, arguments, last, index)
                arguments.pointer = pointer
                result
            }
        } else {
            emptyList()
        }
    }

    override fun handleFailure(context: CommandContext, node: CommandNode, failure: Any) {
        val failureHandler = failureHandlers[failure::class]
        when {
            failureHandler != null -> logger.catchAll("Exception thrown while handling command failure: $failure") {
                failureHandler(CommandFailure(context, node, failure))
            }
            parent != null -> parent.handleFailure(context, node, failure)
            else -> {
                @Suppress("UNCHECKED_CAST")
                val globalFailureHandler = FailureEventRegistry.getHandlerFor(failure::class)
                    as FailureHandler<Any>?

                if (globalFailureHandler == null) {
                    logger.error { "Cannot handle command failure: $failure" }
                } else {
                    logger.catchAll("Exception thrown while handling command failure: $failure") {
                        globalFailureHandler(CommandFailure(context, node, failure))
                    }
                }
            }
        }
    }

    val path: String
        get() = parent?.path?.plus(" ${names.first()}") ?: "/${names.first()}"
}