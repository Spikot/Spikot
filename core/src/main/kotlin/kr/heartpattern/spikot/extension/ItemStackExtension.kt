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

package kr.heartpattern.spikot.extension

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Check [this] is null, air, or amount of 0
 */
@OptIn(ExperimentalContracts::class)
fun ItemStack?.isEmpty(): Boolean {
    contract { returns(false) implies (this@isEmpty != null) }
    return when {
        this == null -> false
        type == Material.AIR -> false
        amount == 0 -> false
        else -> true
    }
}

/**
 * Check [this] is not null, not air, and amount is not 0
 */
@OptIn(ExperimentalContracts::class)
fun ItemStack?.isNotEmpty(): Boolean {
    contract { returns(true) implies (this@isNotEmpty != null) }
    return !isEmpty()
}

/**
 * Check [this] is full; amount is equals or more than max stack size
 */
fun ItemStack.isFull(): Boolean {
    return amount >= maxStackSize
}