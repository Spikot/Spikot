package io.heartpattern.spikot.item.v15

import io.heartpattern.spikot.adapter.Adapter
import io.heartpattern.spikot.item.NMSItemStackAdapter
import io.heartpattern.spikot.nbt.CompoundTag
import io.heartpattern.spikot.nbt.NBTAdapter
import io.heartpattern.spikot.reflection.fieldDelegate
import net.minecraft.server.v1_15_R1.NBTTagCompound
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import net.minecraft.server.v1_15_R1.ItemStack as NMSItemStack

@Adapter(version = "1.15~1.15.2")
class NMSItemStackAdapterImpl : NMSItemStackAdapter {
    override fun loadFromTag(tag: CompoundTag): ItemStack {
        val nms = NMSItemStack.a(tag.rawTag as NBTTagCompound)
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

    private val CraftItemStack.handle by fieldDelegate<CraftItemStack, NMSItemStack?>("handle")
}