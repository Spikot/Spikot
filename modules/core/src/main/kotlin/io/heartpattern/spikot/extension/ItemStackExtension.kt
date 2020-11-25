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

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Check [this] is null, air, or amount of 0
 */
@OptIn(ExperimentalContracts::class)
public fun ItemStack?.isEmpty(): Boolean {
    contract { returns(false) implies (this@isEmpty != null) }
    return when {
        this == null -> true
        type == Material.AIR -> true
        amount == 0 -> true
        else -> false
    }
}

/**
 * Check [this] is not null, not air, and amount is not 0
 */
@OptIn(ExperimentalContracts::class)
public fun ItemStack?.isNotEmpty(): Boolean {
    contract { returns(true) implies (this@isNotEmpty != null) }
    return !isEmpty()
}

/**
 * Check [this] is full; amount is equals or more than max stack size
 */
public fun ItemStack.isFull(): Boolean {
    return amount >= maxStackSize
}