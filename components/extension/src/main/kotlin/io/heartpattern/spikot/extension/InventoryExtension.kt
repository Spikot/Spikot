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

package io.heartpattern.spikot.extension

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.min

/**
 * Check all [items] can be added to [Inventory]
 */
fun Inventory.canAddAll(items: Collection<ItemStack>): Boolean {
    var emptySlot = 0
    val unFullItems = LinkedList<ItemStack>()

    for (item in storageContents) {
        if (item.isEmpty()) {
            emptySlot++
            continue
        }
        if (item.isFull())
            continue

        unFullItems += item
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

            val clone = if (remainAmount == 0) item else item.clone()
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
fun Inventory.giveAddOrNone(items: Collection<ItemStack>): Boolean {
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
fun Inventory.count(item: ItemStack): Int {
    return contents.sumBy {
        if (it.isSimilar(item)) it.amount else 0
    }
}