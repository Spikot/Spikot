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

package io.heartpattern.spikot.extension

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.min

/**
 * Check all [items] can be added to [Inventory]
 */
public fun Inventory.canAddAll(items: Collection<ItemStack>): Boolean {
    var emptySlot = 0
    val unFullItems = LinkedList<ItemStack>()

    for (item in storageContents) {
        if (item.isEmpty()) {
            emptySlot++
            continue
        }
        if (item.isFull())
            continue

        unFullItems += item.clone()
    }

    outer@ for (item in items) {
        if (item.isEmpty())
            continue

        var remainAmount = item.amount
        val iter = unFullItems.iterator()

        while (iter.hasNext()) {
            val curr = iter.next()
            if (!curr.isSimilar(item))
                continue

            val currRemain = curr.maxStackSize - curr.amount
            if (currRemain > remainAmount) {
                curr.amount += remainAmount
                remainAmount = 0
                break
            } else if (currRemain == remainAmount) {
                iter.remove()
                remainAmount = 0
                break
            } else {
                iter.remove()
                remainAmount -= currRemain
            }
        }

        while (remainAmount > 0) {
            if (emptySlot <= 0)
                return false

            val count = min(remainAmount, item.maxStackSize)
            remainAmount -= count

            val clone = item.clone()
            clone.amount = remainAmount

            if (!clone.isFull())
                unFullItems += clone

            emptySlot--
        }
    }

    return true
}

/**
 * Add [items] to [this] inventory. If any of [items] cannot be added to inventory, add none to inventory and return false
 */
public fun Inventory.giveAddOrNone(items: Collection<ItemStack>): Boolean {
    return if (canAddAll(items)) {
        addItem(*items.toTypedArray())
        true
    } else {
        false
    }
}

/**
 * Count number of item similar to [item] in inventory
 */
public fun Inventory.count(item: ItemStack): Int {
    return contents.sumBy {
        if (it.isSimilar(item)) it.amount else 0
    }
}