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
class FancyChat internal constructor() {
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

    val black
        get() = execute { color = BLACK }

    val darkBlue
        get() = execute { color = DARK_BLUE }

    val darkGreen
        get() = execute { color = DARK_GREEN }

    val darkAqua
        get() = execute { color = DARK_AQUA }

    val darkRed
        get() = execute { color = DARK_RED }

    val darkPurple
        get() = execute { color = DARK_PURPLE }

    val gold
        get() = execute { color = GOLD }

    val gray
        get() = execute { color = GRAY }

    val darkGray
        get() = execute { color = DARK_GRAY }

    val blue
        get() = execute { color = BLUE }

    val green
        get() = execute { color = GREEN }

    val aqua
        get() = execute { color = AQUA }

    val red
        get() = execute { color = RED }

    val lightPurple
        get() = execute { color = LIGHT_PURPLE }

    val yellow
        get() = execute { color = YELLOW }

    val white
        get() = execute { color = WHITE }

    val obfuscate
        get() = execute { isObfuscated = true }

    val bold
        get() = execute { isBold = true }

    val strike
        get() = execute { isStrikethrough = true }

    val underline
        get() = execute { isUnderlined = true }

    val italic
        get() = execute { isItalic = true }

    val resetObfuscate
        get() = execute { isObfuscated = false }

    val resetBold
        get() = execute { isBold = false }

    val resetStrike
        get() = execute { isStrikethrough = false }

    val resetUnderline
        get() = execute { isUnderlined = false }

    val resetItalic
        get() = execute { isItalic = false }

    val reset
        get() = execute {
            color = null
            isBold = false
            isItalic = false
            isUnderlined = false
            isStrikethrough = false
            isObfuscated = false
        }

    val resetInsert
        get() = execute {
            insertion = null
        }

    val resetHover
        get() = execute {
            hoverEvent = null
        }

    val resetClick
        get() = execute {
            clickEvent = null
        }

    val resetAll
        get() = reset.resetInsert.resetHover.resetClick

    fun insert(text: String) = execute {
        insertion = text
    }

    fun openUrl(url: String) = execute {
        clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, url)
    }

    fun runCommand(command: String) = execute {
        clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
    }

    fun suggestCommand(command: String) = execute {
        clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)
    }

    fun changePage(page: Int) = execute {
        clickEvent = ClickEvent(ClickEvent.Action.CHANGE_PAGE, page.toString())
    }

    fun hoverText(text: String) = execute {
        hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent(text)))
    }

    fun hoverText(text: Array<BaseComponent>) = execute {
        hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, text)
    }

    fun hoverText(text: FancyChat) = execute {
        hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, text.build())
    }

    fun hoverItem(item: ItemStack) = execute {
        hoverEvent = HoverEvent(
            HoverEvent.Action.SHOW_ITEM,
            arrayOf(TextComponent(item.saveToTag().rawTag.toString()))
        )
    }

    fun hoverEntity(entity: Entity) = execute {
        hoverEvent = HoverEvent(
            HoverEvent.Action.SHOW_ENTITY,
            arrayOf(TextComponent("""{
                "id": "${entity.uniqueId}",
                "type": "minecraft:${entity.type.name.toLowerCase()}"
                ${if (entity.customName != null) ""","name": "${entity.customName}"""" else ""}
            }""".trimIndent()))
        )
    }

    operator fun invoke(text: String) = executeAppend {
        this@executeAppend.text = text
    }

    operator fun invoke(text: FancyChat): FancyChat {
        components.addAll(text.components)
        last = TextComponent()
        last.copyFormatting(components.last)
        return this
    }

    operator fun get(text: FancyChat): FancyChat {
        components.addAll(text.components)
        return this
    }

    fun setAll(
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

    fun copy(): FancyChat {
        val copied = FancyChat()
        copied.components.addAll(components)
        copied.last = TextComponent(last)
        return copied
    }

    fun build(): Array<BaseComponent> = components.toTypedArray()
}