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

package io.heartpattern.spikot.chat

import net.md_5.bungee.api.ChatMessageType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

public val chat: FancyChat
    get() = FancyChat()

public fun CommandSender.sendMessage(chat: FancyChat) {
    spigot().sendMessage(*chat.build())
}

public fun Player.sendActionBar(chat: FancyChat) {
    spigot().sendMessage(ChatMessageType.ACTION_BAR, *chat.build())
}

public fun Player.sendSystem(chat: FancyChat) {
    spigot().sendMessage(ChatMessageType.SYSTEM, *chat.build())
}

public fun List<FancyChat>.joinToLine(): FancyChat {
    if (size == 0)
        return chat

    val iter = iterator()
    val first = iter.next().copy()
    while (iter.hasNext()) {
        first.resetAll("\n")(iter.next())
    }

    return first
}