package io.heartpattern.spikot.adapters.v1_12.nbt

import io.heartpattern.spikot.adapter.Adapter
import io.heartpattern.spikot.Component
import io.heartpattern.spikot.nbt.CompoundTag
import io.heartpattern.spikot.nbt.NBTIOAdapter
import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools
import net.minecraft.server.v1_12_R1.NBTTagCompound
import java.io.DataInputStream
import java.io.DataOutput
import java.io.InputStream
import java.io.OutputStream

@Adapter(adapterOf = NBTIOAdapter::class, version = "1.12.2")
object NBTIOAdapterImpl : Component(), NBTIOAdapter {
    override fun read(input: DataInputStream): CompoundTag {
        return NBTAdapterImpl.wrapCompoundTag(NBTCompressedStreamTools.a(input))
    }

    override fun readCompressed(input: InputStream): CompoundTag {
        return NBTAdapterImpl.wrapCompoundTag(NBTCompressedStreamTools.a(input))
    }

    override fun write(tag: CompoundTag, output: DataOutput) {
        NBTCompressedStreamTools.a(tag.rawTag as NBTTagCompound, output)
    }

    override fun writeCompressed(tag: CompoundTag, output: OutputStream) {
        NBTCompressedStreamTools.a(tag.rawTag as NBTTagCompound, output)
    }
}