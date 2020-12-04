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

import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

public abstract class DetailActionButton(
    icon: ItemStack
) : Button(
    icon
) {
    override final fun onClick(player: Player, clickEvent: InventoryClickEvent) {
        when (clickEvent.click) {
            LEFT -> onLeftClick(player, false)
            SHIFT_LEFT -> onLeftClick(player, true)
            RIGHT -> onRightClick(player, false)
            SHIFT_RIGHT -> onRightClick(player, true)
            DROP -> onDropClick(player, false)
            CONTROL_DROP -> onDropClick(player, true)
            else -> Unit // Nothing
        }
    }

    public open fun onRightClick(player: Player, isShift: Boolean) {

    }

    public open fun onLeftClick(player: Player, isShift: Boolean) {

    }

    public open fun onDropClick(player: Player, isCtrl: Boolean) {

    }
}