package io.heartpattern.spikot.nbt.v16

import io.heartpattern.spikot.adapter.Adapter
import io.heartpattern.spikot.nbt.*
import io.heartpattern.spikot.nbt.Tag
import net.minecraft.server.v1_16_R2.*

@Adapter(version = "1.16~1.16.4")
class NBTAdapterImpl : NBTAdapter {
    override fun createEndTag(): EndTag = wrapEndTag(NBTTagEnd.b)
    override fun createByteTag(value: Byte): ByteTag = ByteTagImpl(NBTTagByte.a(value))
    override fun createShortTag(value: Short): ShortTag = ShortTagImpl(NBTTagShort.a(value))
    override fun createIntTag(value: Int): IntTag = IntTagImpl(NBTTagInt.a(value))
    override fun createLongTag(value: Long): LongTag = LongTagImpl(NBTTagLong.a(value))
    override fun createFloatTag(value: Float): FloatTag = FloatTagImpl(NBTTagFloat.a(value))
    override fun createDoubleTag(value: Double): DoubleTag = DoubleTagImpl(NBTTagDouble.a(value))
    override fun createByteArrayTag(value: ByteArray): ByteArrayTag = ByteArrayTagImpl(NBTTagByteArray(value))
    override fun createByteArrayTag(value: List<Byte>): ByteArrayTag = ByteArrayTagImpl(NBTTagByteArray(value))
    override fun createStringTag(): StringTag = StringTagImpl(NBTTagString.a(""))
    override fun createStringTag(value: String): StringTag = StringTagImpl(NBTTagString.a(value))
    override fun createListTag(): ListTag = ListTagImpl(NBTTagList())
    override fun createCompoundTag(): CompoundTag = CompoundTagImpl(NBTTagCompound())
    override fun createIntArrayTag(value: IntArray): IntArrayTag = IntArrayTagImpl(NBTTagIntArray(value))
    override fun createIntArrayTag(value: List<Int>): IntArrayTag = IntArrayTagImpl(NBTTagIntArray(value))
    override fun createLongArrayTag(value: LongArray): LongArrayTag = LongArrayTagImpl(NBTTagLongArray(value))
    override fun createLongArrayTag(value: List<Long>): LongArrayTag = LongArrayTagImpl(NBTTagLongArray(value))

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createTag(type: TagType<T>): Tag<T> {
        return when (type) {
            TagType.END -> createEndTag()
            TagType.BYTE -> createByteTag(0)
            TagType.SHORT -> createShortTag(0)
            TagType.INT -> createIntTag(0)
            TagType.LONG -> createLongTag(0)
            TagType.FLOAT -> createFloatTag(0f)
            TagType.DOUBLE -> createDoubleTag(0.0)
            TagType.BYTE_ARRAY -> createByteTag(0)
            TagType.STRING -> createStringTag()
            TagType.LIST -> createListTag()
            TagType.COMPOUND -> createCompoundTag()
            TagType.INT_ARRAY -> createIntArrayTag(listOf())
            TagType.LONG_ARRAY -> createLongArrayTag(listOf())
            TagType.MISC -> throw IllegalArgumentException("Illegal tag type: ${type}")

        } as Tag<T>
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