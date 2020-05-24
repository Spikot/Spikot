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
import io.heartpattern.spikot.command.argument.CommandArgument
import io.heartpattern.spikot.command.failure.FailureHandler
import io.heartpattern.spikot.extension.getValue
import io.heartpattern.spikot.extension.setValue
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass

@CommandDslMarker
class CommandDSL internal constructor(
    names: List<String>,
    priority: Int,
    parent: CommandDSL?
) {
    private val children = LinkedList<CommandNode>()
    private val failureHandlers = HashMap<KClass<*>, FailureHandler<Any>>()
    internal val node: InternalCommandNode = InternalCommandNode(parent?.node, names, priority, children, failureHandlers)

    var description: FancyChat by node::description

    fun child(vararg name: String, priority: Int = -10000, configure: CommandDSL.() -> Unit) {
        val dsl = CommandDSL(name.toList(), priority, this)
        dsl.configure()
        children.add(dsl.node)
        children.sortedBy { it.priority }
    }

    fun executes(vararg argument: CommandArgument<*>, priority: Int = 0, handler: CommandContext.(List<Any?>) -> Unit) {
        children.add(LeafCommandNode(node, priority, argument.toList(), handler))
        children.sortedBy { it.priority }
    }

    inline fun <reified T : Any> failureHandler(noinline handler: FailureHandler<T>) {
        failureHandler(T::class, handler)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> failureHandler(type: KClass<T>, handler: FailureHandler<T>) {
        failureHandlers[type] = handler as FailureHandler<Any>
    }
}

