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

package io.heartpattern.spikot.item

import io.heartpattern.spikot.adapter.getAdapterOf
import io.heartpattern.spikot.nbt.CompoundTag
import org.bukkit.inventory.ItemStack

interface NMSItemStackAdapter {
    fun loadFromTag(tag: CompoundTag): ItemStack
    fun saveToTag(item: ItemStack): CompoundTag
    fun getTag(item: ItemStack): CompoundTag?
    fun hasTag(item: ItemStack): Boolean
    fun setTag(item: ItemStack, tag: CompoundTag?)
    fun isCraftItemStack(item: ItemStack): Boolean
    fun isNMSCraftItemStack(item: ItemStack): Boolean
    fun toCraftItemStack(item: ItemStack): ItemStack

    companion object Impl : NMSItemStackAdapter by getAdapterOf()
}