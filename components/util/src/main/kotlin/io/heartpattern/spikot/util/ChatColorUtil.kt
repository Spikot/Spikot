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

package io.heartpattern.spikot.util

import org.bukkit.ChatColor

operator fun ChatColor.plus(message: String) = this.toString() + message
operator fun ChatColor.plus(color: ChatColor) = this.toString() + color.toString()

/**
 * Translate alternative color code. i.e) &a to §a
 */
fun String.translateAlternateColorCodes(alternate: Char) = ChatColor.translateAlternateColorCodes(alternate, this)

/**
 * ColorCode remain at the end of string
 */
fun String.lastColorCode(): CombinedChatColor {
    var color: Char? = null
    var strike = false
    var obfuscate = false
    var bold = false
    var underline = false
    var italic = false
    var reset = false

    // Fast matching if message is well-formed
    fun fast(): Boolean {
        for (i in indices.reversed()) {
            // Not a color code
            if (this[i] != '§' || i >= length - 1)
                continue

            // Malformed message (Retry with slow method)
            if (i > 0 && this[i - 1] == '§')
                return false

            when (this[i + 1]) {
                in "0123456789abcdef" -> if (color == null) color = this[i + 1]
                'k' -> obfuscate = true
                'l' -> bold = true
                'm' -> strike = true
                'n' -> underline = true
                'o' -> italic = true
                'r' -> reset = true
            }

            // Any color code before reset is ignored
            if (reset)
                return true

            // Every color code is defined; any color code before this is rewritten
            if (color != null && strike && obfuscate && bold && underline && italic)
                return true
        }

        return true
    }

    // Slower matching
    fun slow() {
        var isColorCode = false
        for (char in this) {
            if (isColorCode) {
                when (char) {
                    in "0123456789abcdef" -> color = char
                    'k' -> obfuscate = true
                    'l' -> bold = true
                    'm' -> strike = true
                    'n' -> underline = true
                    'o' -> italic = true
                    'r' -> {
                        color = null
                        obfuscate = false
                        bold = false
                        strike = false
                        underline = false
                        italic = false
                        reset = true
                    }
                }
                isColorCode = false
            } else if (char == '§')
                isColorCode = true
        }
    }

    if (!fast()) {
        color = null
        obfuscate = false
        bold = false
        strike = false
        underline = false
        italic = false
        reset = false
        slow()
    }

    return CombinedChatColor(
        color?.let { ChatColor.getByChar(it) },
        obfuscate,
        bold,
        strike,
        underline,
        italic,
        reset
    )
}

/**
 * Represent combination of chat color
 */
data class CombinedChatColor(
    val color: ChatColor?,
    val obfuscate: Boolean,
    val bold: Boolean,
    val strikeThrough: Boolean,
    val underline: Boolean,
    val italic: Boolean,
    val reset: Boolean
) {
    override fun toString(): String {
        val builder = StringBuilder()
        if (reset)
            builder.append("§r")
        if (obfuscate)
            builder.append("§k")
        if (bold)
            builder.append("§l")
        if (strikeThrough)
            builder.append("§m")
        if (underline)
            builder.append("§n")
        if (italic)
            builder.append("§o")
        if (color != null)
            builder.append(color.toString())

        return builder.toString()
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun toChatColors(): List<ChatColor> {
        return buildList<ChatColor>(7) {
            if (reset)
                add(ChatColor.RESET)
            if (obfuscate)
                add(ChatColor.MAGIC)
            if (bold)
                add(ChatColor.BOLD)
            if (strikeThrough)
                add(ChatColor.STRIKETHROUGH)
            if (underline)
                add(ChatColor.UNDERLINE)
            if (italic)
                add(ChatColor.ITALIC)
            if (color != null)
                add(color)
        }
    }
}