package io.heartpattern.spikot.nbt.v15

import io.heartpattern.spikot.adapter.Adapter
import io.heartpattern.spikot.nbt.CompoundTag
import io.heartpattern.spikot.nbt.NBTAdapter
import io.heartpattern.spikot.nbt.NBTIOAdapter
import net.minecraft.server.v1_15_R1.NBTCompressedStreamTools
import net.minecraft.server.v1_15_R1.NBTTagCompound
import java.io.DataInputStream
import java.io.DataOutput
import java.io.InputStream
import java.io.OutputStream

@Adapter(version = "1.15~1.15.2")
class NBTIOAdapterImpl(
    val adapter: NBTAdapter
) : NBTIOAdapter {
    override fun read(input: DataInputStream): CompoundTag {
        return adapter.wrapCompoundTag(NBTCompressedStreamTools.a(input))
    }

    override fun readCompressed(input: InputStream): CompoundTag {
        return adapter.wrapCompoundTag(NBTCompressedStreamTools.a(input))
    }

    override fun write(tag: CompoundTag, output: DataOutput) {
        NBTCompressedStreamTools.a(tag.rawTag as NBTTagCompound, output)
    }

    override fun writeCompressed(tag: CompoundTag, output: OutputStream) {
        NBTCompressedStreamTools.a(tag.rawTag as NBTTagCompound, output)
    }
}