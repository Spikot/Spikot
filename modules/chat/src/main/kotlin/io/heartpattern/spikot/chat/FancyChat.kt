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

import io.heartpattern.spikot.item.saveToTag
import net.md_5.bungee.api.ChatColor.*
import net.md_5.bungee.api.chat.*
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * Chat builder dsl
 */
public class FancyChat internal constructor() {
    private val components = LinkedList<BaseComponent>()
    private var last: TextComponent = TextComponent("")

    private inline fun execute(block: TextComponent.() -> Unit): FancyChat {
        last.block()
        return this
    }

    private inline fun executeAppend(block: TextComponent.() -> Unit): FancyChat {
        last.block()
        components.add(last)
        last = TextComponent()
        last.copyFormatting(components.last())
        return this
    }

    public val black: FancyChat
        get() = execute { color = BLACK }

    public val darkBlue: FancyChat
        get() = execute { color = DARK_BLUE }

    public val darkGreen: FancyChat
        get() = execute { color = DARK_GREEN }

    public val darkAqua: FancyChat
        get() = execute { color = DARK_AQUA }

    public val darkRed: FancyChat
        get() = execute { color = DARK_RED }

    public val darkPurple: FancyChat
        get() = execute { color = DARK_PURPLE }

    public val gold: FancyChat
        get() = execute { color = GOLD }

    public val gray: FancyChat
        get() = execute { color = GRAY }

    public val darkGray: FancyChat
        get() = execute { color = DARK_GRAY }

    public val blue: FancyChat
        get() = execute { color = BLUE }

    public val green: FancyChat
        get() = execute { color = GREEN }

    public val aqua: FancyChat
        get() = execute { color = AQUA }

    public val red: FancyChat
        get() = execute { color = RED }

    public val lightPurple: FancyChat
        get() = execute { color = LIGHT_PURPLE }

    public val yellow: FancyChat
        get() = execute { color = YELLOW }

    public val white: FancyChat
        get() = execute { color = WHITE }

    public val obfuscate: FancyChat
        get() = execute { isObfuscated = true }

    public val bold: FancyChat
        get() = execute { isBold = true }

    public val strike: FancyChat
        get() = execute { isStrikethrough = true }

    public val underline: FancyChat
        get() = execute { isUnderlined = true }

    public val italic: FancyChat
        get() = execute { isItalic = true }

    public val resetObfuscate: FancyChat
        get() = execute { isObfuscated = false }

    public val resetBold: FancyChat
        get() = execute { isBold = false }

    public val resetStrike: FancyChat
        get() = execute { isStrikethrough = false }

    public val resetUnderline: FancyChat
        get() = execute { isUnderlined = false }

    public val resetItalic: FancyChat
        get() = execute { isItalic = false }

    public val reset: FancyChat
        get() = execute {
            color = null
            isBold = false
            isItalic = false
            isUnderlined = false
            isStrikethrough = false
            isObfuscated = false
        }

    public val resetInsert: FancyChat
        get() = execute {
            insertion = null
        }

    public val resetHover: FancyChat
        get() = execute {
            hoverEvent = null
        }

    public val resetClick: FancyChat
        get() = execute {
            clickEvent = null
        }

    public val resetAll: FancyChat
        get() = reset.resetInsert.resetHover.resetClick

    public fun insert(text: String): FancyChat = execute {
        insertion = text
    }

    public fun openUrl(url: String): FancyChat = execute {
        clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, url)
    }

    public fun runCommand(command: String): FancyChat = execute {
        clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
    }

    public fun suggestCommand(command: String): FancyChat = execute {
        clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)
    }

    public fun changePage(page: Int): FancyChat = execute {
        clickEvent = ClickEvent(ClickEvent.Action.CHANGE_PAGE, page.toString())
    }

    // For backward compatibility, do not use Content.
    @Suppress("DEPRECATION")
    public fun hoverText(text: String): FancyChat = execute {
        hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent(text)))
    }

    @Suppress("DEPRECATION")
    public fun hoverText(text: Array<BaseComponent>): FancyChat = execute {
        hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, text)
    }

    @Suppress("DEPRECATION")
    public fun hoverText(text: FancyChat): FancyChat = execute {
        hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, text.build())
    }

    @Suppress("DEPRECATION")
    public fun hoverItem(item: ItemStack): FancyChat = execute {
        hoverEvent = HoverEvent(
            HoverEvent.Action.SHOW_ITEM,
            arrayOf(TextComponent(item.saveToTag().rawTag.toString()))
        )
    }

    @Suppress("DEPRECATION")
    public fun hoverEntity(entity: Entity): FancyChat = execute {
        hoverEvent = HoverEvent(
            HoverEvent.Action.SHOW_ENTITY,
            arrayOf(TextComponent("""{
                "id": "${entity.uniqueId}",
                "type": "minecraft:${entity.type.name.toLowerCase()}"
                ${if (entity.customName != null) ""","name": "${entity.customName}"""" else ""}
            }""".trimIndent()))
        )
    }

    public operator fun invoke(text: String): FancyChat = executeAppend {
        this@executeAppend.text = text
    }

    public operator fun invoke(text: FancyChat): FancyChat {
        components.addAll(text.components)
        last = TextComponent()
        last.copyFormatting(components.last)
        return this
    }

    public operator fun get(text: FancyChat): FancyChat {
        components.addAll(text.components)
        return this
    }

    public fun setAll(
        retention: ComponentBuilder.FormatRetention = ComponentBuilder.FormatRetention.ALL,
        replace: Boolean = true,
        configure: FancyChat.() -> Unit
    ): FancyChat {
        val fancy = FancyChat()
        fancy.configure()
        components.forEach {
            it.copyFormatting(fancy.last, retention, replace)
        }
        return this
    }

    public fun copy(): FancyChat {
        val copied = FancyChat()
        copied.components.addAll(components)
        copied.last = TextComponent(last)
        return copied
    }

    public fun build(): Array<BaseComponent> = components.toTypedArray()
}