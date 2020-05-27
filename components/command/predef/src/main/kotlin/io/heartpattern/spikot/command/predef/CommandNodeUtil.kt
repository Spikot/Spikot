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

package io.heartpattern.spikot.command.predef

import io.heartpattern.spikot.chat.FancyChat
import io.heartpattern.spikot.chat.chat
import io.heartpattern.spikot.command.CommandNode
import io.heartpattern.spikot.command.InternalCommandNode
import io.heartpattern.spikot.command.LeafCommandNode
import java.util.*

val CommandNode.parents: List<InternalCommandNode>
    get() {
        val parents = LinkedList<InternalCommandNode>()

        var p: InternalCommandNode? = parent
        while (p != null) {
            parents.addFirst(p)
            p = p.parent
        }

        return parents
    }

fun InternalCommandNode.toChat(): FancyChat {
    return if (parent != null) {
        chat
            .hoverText(description)
            .suggestCommand(path)
            .invoke("/${names.first()}")
    } else {
        parent!!.toChat()
            .reset
            .hoverText(description)
            .suggestCommand(path)
            .invoke(" ${names.first()}")
    }
}

fun LeafCommandNode.toChat(): FancyChat {
    val result = parent.toChat()

    for (argument in arguments) {
        result
            .reset
            .hoverText(argument.description)
            .invoke("${argument.name} ")
    }

    return result
}
