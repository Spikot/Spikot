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
import io.heartpattern.spikot.chat.chat
import io.heartpattern.spikot.command.argument.ArgumentIterator
import io.heartpattern.spikot.command.argument.CommandArgument
import io.heartpattern.spikot.command.argument.CommandArgumentBuilder
import io.heartpattern.spikot.command.failure.FailureHandler
import org.bukkit.permissions.Permission
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.reflect.KClass

@CommandDslMarker
class CommandDSL internal constructor(
    private val names: List<String>
) {
    private val internalChildren = LinkedList<CommandDSL>()
    private val leafChildren = LinkedList<LeafCommandDefinition>()
    private val failureHandlers = HashMap<KClass<*>, FailureHandler<Any>>()

    var priority: Int = 0
    var description: FancyChat = chat
    var permission: Permission? = null

    val argument: CommandArgumentBuilder<ArgumentIterator>
        get() = CommandArgumentBuilder()

    fun child(vararg name: String, configure: CommandDSL.() -> Unit) {
        val dsl = CommandDSL(name.toList())
        dsl.configure()
        internalChildren.add(dsl)
    }

    fun executes(
        vararg argument: CommandArgument<*>,
        permission: Permission? = null,
        priority: Int = 0,
        handler: CommandContext.(List<Any?>) -> Unit
    ) {
        leafChildren.add(LeafCommandDefinition(priority, permission, argument.toList(), handler))
    }

    inline fun <reified T : Any> failureHandler(noinline handler: FailureHandler<T>) {
        failureHandler(T::class, handler)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> failureHandler(type: KClass<T>, handler: FailureHandler<T>) {
        failureHandlers[type] = handler as FailureHandler<Any>
    }

    internal fun build(parent: InternalCommandNode?): InternalCommandNode {
        val children = ArrayList<CommandNode>(internalChildren.size + leafChildren.size)
        val node = InternalCommandNode(parent, names, description, permission, priority, children, failureHandlers)
        for (child in internalChildren)
            children.add(child.build(node))

        for ((priority, permission, argument, handler) in leafChildren)
            children.add(LeafCommandNode(node, permission, priority, argument, handler))

        return node
    }

    private data class LeafCommandDefinition(
        val priority: Int,
        val permission: Permission?,
        val argument: List<CommandArgument<*>>,
        val handler: (CommandContext.(List<Any?>) -> Unit)
    )
}

