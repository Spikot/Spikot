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
import kr.heartpattern.spikot.nbt.*
import net.minecraft.server.v1_12_R1.*

@Adapter(adapterOf = NBTAdapter::class, version = "1.12.2")
object NBTAdapterImpl : Component(), NBTAdapter {
    private val createTagMethod = NBTBase::class.java.getDeclaredMethod("createTag", Byte::class.java)

    init {
        createTagMethod.isAccessible = true
    }

    override fun createEndTag(): EndTag = createTag(TagType.END) as EndTag
    override fun createByteTag(value: Byte): ByteTag = ByteTagImpl(NBTTagByte(value))
    override fun createShortTag(value: Short): ShortTag = ShortTagImpl(NBTTagShort(value))
    override fun createIntTag(value: Int): IntTag = IntTagImpl(NBTTagInt(value))
    override fun createLongTag(value: Long): LongTag = LongTagImpl(NBTTagLong(value))
    override fun createFloatTag(value: Float): FloatTag = FloatTagImpl(NBTTagFloat(value))
    override fun createDoubleTag(value: Double): DoubleTag = DoubleTagImpl(NBTTagDouble(value))
    override fun createByteArrayTag(value: ByteArray): ByteArrayTag = ByteArrayTagImpl(NBTTagByteArray(value))
    override fun createByteArrayTag(value: List<Byte>): ByteArrayTag = ByteArrayTagImpl(NBTTagByteArray(value))
    override fun createStringTag(): StringTag = StringTagImpl(NBTTagString())
    override fun createStringTag(value: String): StringTag = StringTagImpl(NBTTagString(value))
    override fun createListTag(): ListTag = ListTagImpl(NBTTagList())
    override fun createCompoundTag(): CompoundTag = CompoundTagImpl(NBTTagCompound())
    override fun createIntArrayTag(value: IntArray): IntArrayTag = IntArrayTagImpl(NBTTagIntArray(value))
    override fun createIntArrayTag(value: List<Int>): IntArrayTag = IntArrayTagImpl(NBTTagIntArray(value))
    override fun createLongArrayTag(value: LongArray): LongArrayTag = LongArrayTagImpl(NBTTagLongArray(value))
    override fun createLongArrayTag(value: List<Long>): LongArrayTag = LongArrayTagImpl(NBTTagLongArray(value))

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createTag(type: TagType<T>): Tag<T> {
        return wrapTag(createTagMethod.invoke(null, type.id.toByte())) as Tag<T>
    }

    override fun wrapEndTag(tag: Any): EndTag = EndTagImpl(tag as NBTTagEnd)
    override fun wrapByteTag(tag: Any): ByteTag = ByteTagImpl(tag as NBTTagByte)
    override fun wrapShortTag(tag: Any): ShortTag = ShortTagImpl(tag as NBTTagShort)
    override fun wrapIntTag(tag: Any): IntTag = IntTagImpl(tag as NBTTagInt)
    override fun wrapLongTag(tag: Any): LongTag = LongTagImpl(tag as NBTTagLong)
    override fun wrapFloatTag(tag: Any): FloatTag = FloatTagImpl(tag as NBTTagFloat)
    override fun wrapDoubleTag(tag: Any): DoubleTag = DoubleTagImpl(tag as NBTTagDouble)
    override fun wrapByteArrayTag(tag: Any): ByteArrayTag = ByteArrayTagImpl(tag as NBTTagByteArray)
    override fun wrapStringTag(tag: Any): StringTag = StringTagImpl(tag as NBTTagString)
    override fun wrapListTag(tag: Any): ListTag = ListTagImpl(tag as NBTTagList)
    override fun wrapCompoundTag(tag: Any): CompoundTag = CompoundTagImpl(tag as NBTTagCompound)
    override fun wrapIntArrayTag(tag: Any): IntArrayTag = IntArrayTagImpl(tag as NBTTagIntArray)
    override fun wrapLongArrayTag(tag: Any): LongArrayTag = LongArrayTagImpl(tag as NBTTagLongArray)

    override fun wrapTag(tag: Any): Tag<*> {
        return when (tag) {
            is NBTTagEnd -> EndTagImpl(tag)
            is NBTTagByte -> ByteTagImpl(tag)
            is NBTTagShort -> ShortTagImpl(tag)
            is NBTTagInt -> IntTagImpl(tag)
            is NBTTagLong -> LongTagImpl(tag)
            is NBTTagFloat -> FloatTagImpl(tag)
            is NBTTagDouble -> DoubleTagImpl(tag)
            is NBTTagByteArray -> ByteArrayTagImpl(tag)
            is NBTTagString -> StringTagImpl(tag)
            is NBTTagList -> ListTagImpl(tag)
            is NBTTagCompound -> CompoundTagImpl(tag)
            is NBTTagIntArray -> IntArrayTagImpl(tag)
            is NBTTagLongArray -> LongArrayTagImpl(tag)
            else -> throw IllegalArgumentException("Unknown tag type: ${tag.javaClass.name}")
        }
    }
}