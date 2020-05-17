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

package kr.heartpattern.spikot.adapters.v1_12.nbt

import kr.heartpattern.spikot.adapter.Adapter
import kr.heartpattern.spikot.component.Component
import kr.heartpattern.spikot.nbt.CompoundTag
import kr.heartpattern.spikot.nbt.NBTIOAdapter
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