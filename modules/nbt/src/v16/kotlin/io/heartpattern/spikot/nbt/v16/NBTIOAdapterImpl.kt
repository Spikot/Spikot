package io.heartpattern.spikot.nbt.v16

import io.heartpattern.spikot.adapter.Adapter
import io.heartpattern.spikot.nbt.CompoundTag
import io.heartpattern.spikot.nbt.NBTAdapter
import io.heartpattern.spikot.nbt.NBTIOAdapter
import net.minecraft.server.v1_16_R2.NBTCompressedStreamTools
import net.minecraft.server.v1_16_R2.NBTTagCompound
import java.io.*

@Adapter(version = "1.16~1.16.4")
class NBTIOAdapterImpl(
    val adapter: NBTAdapter
) : NBTIOAdapter {
    override fun read(input: DataInputStream): CompoundTag {
        return adapter.wrapCompoundTag(NBTCompressedStreamTools.a(input as DataInput))
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