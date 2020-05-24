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

package io.heartpattern.spikot.chat

import net.md_5.bungee.api.ChatMessageType
import org.bukkit.entity.Player

val chat
    get() = FancyChat()

fun Player.sendMessage(builder: FancyChat) {
    spigot().sendMessage(ChatMessageType.CHAT, *builder.build())
}

fun Player.sendActionBar(builder: FancyChat) {
    spigot().sendMessage(ChatMessageType.ACTION_BAR, *builder.build())
}

fun Player.sendSystem(builder: FancyChat) {
    spigot().sendMessage(ChatMessageType.SYSTEM, *builder.build())
}

fun List<FancyChat>.joinToLine(): FancyChat {
    if (size == 0)
        return chat

    val iter = iterator()
    val first = iter.next().copy()
    while (iter.hasNext()) {
        first.resetAll("\n")(iter.next())
    }

    return first
}