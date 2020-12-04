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

package io.heartpattern.spikot.menu

import io.heartpattern.spikot.SpikotPlugin
import io.heartpattern.spikot.bean.*
import io.heartpattern.spikot.bean.definition.BeanDefinition
import io.heartpattern.spikot.scope.ScopeInstance
import io.heartpattern.spikot.scope.SimpleScopeInstance
import io.heartpattern.spikot.util.runLastSync
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

public abstract class Menu(
    public val size: Int,
    public val title: String = ""
) {
    private lateinit var definition: BeanDefinition
    private lateinit var scope: ScopeInstance
    private var ignoreOpen = false
    private val buttons = HashMap<Int, ButtonHolder>()
    protected val plugin: SpikotPlugin by inject()
    protected val player: Player by inject<Player>()

    protected val inventory: Inventory by lazy {
        Bukkit.createInventory(null, size, title)
    }

    public open fun open() {}
    public open fun onClick(clickEvent: InventoryClickEvent) {}
    public open fun close(): Boolean {
        return true
    }

    protected fun setButton(index: Int, button: Button?) {
        val previousHolder = buttons[index]
        if (previousHolder != null && button != previousHolder.button) {
            previousHolder.button.unregisterListener(previousHolder)
            inventory.setItem(index, null)
            buttons.remove(index)
        }
        if (button != null && button != previousHolder?.button) {
            val holder = ButtonHolder(index, button)
            buttons[index] = holder
            inventory.setItem(index, button.icon)
            button.registerListener(holder)
        }
    }

    protected fun setButton(index: Int, icon: ItemStack, handler: (InventoryClickEvent) -> Unit) {
        setButton(index, object : Button(icon) {
            override fun onClick(player: Player, clickEvent: InventoryClickEvent) {
                handler(clickEvent)
            }
        })
    }

    internal fun doOpen(definition: BeanDefinition, registry: BeanRegistry) {
        this.definition = definition
        scope = SimpleScopeInstance(
            "menu-${this}",
            "menu",
            registry.getBean(BeanDescription.fromType(SpikotPlugin::class)) as SpikotPlugin,
            registry
        )
        definition.initializeBean(this@Menu, scope)
        player.openInventory(inventory)
    }

    @EventHandler
    internal fun InventoryOpenEvent.handleInventoryOpen() {
        if (inventory == this@Menu.inventory && !ignoreOpen)
            open()

        ignoreOpen = false
    }

    @EventHandler
    internal fun InventoryClickEvent.handleInventoryClick() {
        if (inventory == this@Menu.inventory) {
            onClick(this)
            isCancelled = true
            buttons[slot]?.button?.onClick(player, this)
        }
    }

    @EventHandler
    internal fun InventoryCloseEvent.handleInventoryClose() {
        if (inventory == this@Menu.inventory) {
            val result = close()
            if (!result) {
                ignoreOpen = true
                plugin.runLastSync {
                    player.openInventory(inventory)
                }
            } else {
                buttons.values.forEach {
                    it.button.unregisterListener(it)
                }
                definition.destroyBean(this@Menu, scope)
            }
        }
    }

    private inner class ButtonHolder(val index: Int, val button: Button) : Button.Listener {
        override fun update(item: ItemStack) {
            inventory.setItem(index, item)
        }
    }
}