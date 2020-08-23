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

package io.heartpattern.spikot.util

import org.bukkit.ChatColor

public operator fun ChatColor.plus(message: String): String = this.toString() + message
public operator fun ChatColor.plus(color: ChatColor): String = this.toString() + color.toString()

/**
 * Translate alternative color code. i.e) &a to §a
 */
public fun String.translateAlternateColorCodes(alternate: Char): String = ChatColor.translateAlternateColorCodes(alternate, this)

/**
 * ColorCode remain at the end of string
 */
public fun String.lastColorCode(): CombinedChatColor {
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
public data class CombinedChatColor(
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
    public fun toChatColors(): List<ChatColor> {
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