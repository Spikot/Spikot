package io.heartpattern.spikot.adapters.v1_12.item

import io.heartpattern.spikot.adapter.Adapter
import io.heartpattern.spikot.Component
import io.heartpattern.spikot.item.NMSItemStackAdapter
import io.heartpattern.spikot.nbt.CompoundTag
import io.heartpattern.spikot.nbt.NBTAdapter
import io.heartpattern.spikot.reflection.fieldDelegate
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