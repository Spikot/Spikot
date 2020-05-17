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

package kr.heartpattern.spikot.adapters.v1_12.item

import kr.heartpattern.spikot.adapter.Adapter
import kr.heartpattern.spikot.component.Component
import kr.heartpattern.spikot.item.NMSItemStackAdapter
import kr.heartpattern.spikot.nbt.CompoundTag
import kr.heartpattern.spikot.nbt.NBTAdapter
import kr.heartpattern.spikot.reflection.fieldDelegate
import net.minecraft.server.v1_12_R1.NBTTagCompound
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import net.minecraft.server.v1_12_R1.ItemStack as NMSItemStack

@Adapter(adapterOf = NMSItemStackAdapter::class, version = "1.12.2")
object NMSItemStackAdapterImpl : Component(), NMSItemStackAdapter {
    override fun loadFromTag(tag: CompoundTag): ItemStack {
        val nms = NMSItemStack(tag.rawTag as NBTTagCompound)
        return CraftItemStack.asCraftMirror(nms)
    }

    override fun saveToTag(item: ItemStack): CompoundTag {
        return NBTAdapter.wrapCompoundTag((item as CraftItemStack).handle!!.save(NBTTagCompound()))
    }

    override fun getTag(item: ItemStack): CompoundTag? {
        return (item as CraftItemStack).handle!!.tag?.let { NBTAdapter.wrapCompoundTag(it) }
    }

    override fun hasTag(item: ItemStack): Boolean {
        return (item as? CraftItemStack)?.handle?.tag != null
    }

    override fun setTag(item: ItemStack, tag: CompoundTag?) {
        (item as CraftItemStack).handle!!.tag = tag?.rawTag as NBTTagCompound?
    }

    override fun isCraftItemStack(item: ItemStack): Boolean {
        return item is CraftItemStack
    }

    override fun isNMSCraftItemStack(item: ItemStack): Boolean {
        return (item as? CraftItemStack)?.handle != null
    }

    override fun toCraftItemStack(item: ItemStack): ItemStack {
        return CraftItemStack.asCraftMirror(CraftItemStack.asNMSCopy(item))
    }

    val CraftItemStack.handle by fieldDelegate<CraftItemStack, NMSItemStack?>("handle")
}