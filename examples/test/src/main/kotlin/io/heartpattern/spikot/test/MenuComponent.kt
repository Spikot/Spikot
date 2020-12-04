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

package io.heartpattern.spikot.test

import io.heartpattern.spikot.SpikotPlugin
import io.heartpattern.spikot.command.AdvancedCommandExecutor
import io.heartpattern.spikot.command.CommandComponent
import io.heartpattern.spikot.menu.Button
import io.heartpattern.spikot.menu.Menu
import io.heartpattern.spikot.menu.openMenu
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

@CommandComponent("open_menu")
class MenuComponent(
    val plugin: SpikotPlugin
) : AdvancedCommandExecutor() {
    override fun CommandExecutorContext.onCommand() {
        (sender as Player).openMenu(plugin, object : Menu(54, "Test menu") {
            private var allowClose = false
            override fun open() {
                player.sendMessage("Open")
                setButton(30, ItemStack(Material.STONE)) {
                    player.sendMessage("Index 30")
                }

                setButton(31, object : Button(ItemStack(Material.STONE)) {
                    override fun onClick(player: Player, clickEvent: InventoryClickEvent) {
                        icon = ItemStack(Material.values().random())
                    }
                })

                setButton(53,  ItemStack(Material.BARRIER)){
                    allowClose = true
                    player.closeInventory()
                }
            }

            override fun close(): Boolean {
                return allowClose
            }
        })
    }
}