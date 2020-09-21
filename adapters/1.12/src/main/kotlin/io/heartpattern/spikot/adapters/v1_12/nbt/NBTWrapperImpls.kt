package io.heartpattern.spikot.adapters.v1_12.nbt

import io.heartpattern.spikot.nbt.*
import io.heartpattern.spikot.reflection.fieldDelegate
import net.minecraft.server.v1_12_R1.*
import java.util.*

private val NBTTagLongArray.value by fieldDelegate<NBTTagLongArray, LongArray>("b")

private fun NBTBase.wrap(): Tag<*> = NBTAdapterImpl.wrapTag(this)

data class EndTagImpl(override val rawTag: NBTTagEnd) : EndTag {
    override val value: Unit
        get() = Unit

    override fun copy(): EndTag = EndTagImpl(rawTag.c())
}

data class ByteTagImpl(override val rawTag: NBTTagByte) : ByteTag {
    override val value: Byte
        get() = rawTag.g()

    override fun copy(): ByteTag = ByteTagImpl(rawTag.c())
}

data class ShortTagImpl(override val rawTag: NBTTagShort) : ShortTag {
    override val value: Short
        get() = rawTag.f()

    override fun copy(): ShortTag = ShortTagImpl(rawTag.c())
}

data class IntTagImpl(override val rawTag: NBTTagInt) : IntTag {
    override val value: Int
        get() = rawTag.e()

    override fun copy(): IntTag = IntTagImpl(rawTag.c())
}

data class LongTagImpl(override val rawTag: NBTTagLong) : LongTag {
    override val value: Long
        get() = rawTag.d()

    override fun copy(): LongTag = LongTagImpl(rawTag.c())
}

data class FloatTagImpl(override val rawTag: NBTTagFloat) : FloatTag {
    override val value: Float
        get() = rawTag.i()

    override fun copy(): FloatTag = FloatTagImpl(rawTag.c())
}

data class DoubleTagImpl(override val rawTag: NBTTagDouble) : DoubleTag {
    override val value: Double
        get() = rawTag.asDouble()

    override fun copy(): DoubleTag = DoubleTagImpl(rawTag.c())
}

data class ByteArrayTagImpl(override val rawTag: NBTTagByteArray) : ByteArrayTag {
    override val value: ByteArray
        get() = rawTag.c()

    override fun copy(): ByteArrayTag = ByteArrayTagImpl(rawTag.clone() as NBTTagByteArray)

    override fun isEmpty(): Boolean = rawTag.isEmpty
}

data class StringTagImpl(override val rawTag: NBTTagString) : StringTag {
    override val value: String
        get() = rawTag.c_()

    override fun copy(): StringTag = StringTagImpl(rawTag.c())

    override fun isEmpty(): Boolean = rawTag.isEmpty
}

data class ListTagImpl(override val rawTag: NBTTagList) : ListTag {
    @OptIn(ExperimentalStdlibApi::class)
    override val value: List<Tag<*>>
        get() = buildList(rawTag.size()) { repeat(rawTag.size()) { add(rawTag[it].wrap()) } }

    override fun copy(): ListTag = ListTagImpl(rawTag.d())
    override fun add(element: Tag<*>) = rawTag.add(element.rawTag as NBTBase)
    override fun add(index: Int, element: Tag<*>) = rawTag.a(index, element.rawTag as NBTBase)
    override fun remove(index: Int): Tag<*> = NBTAdapterImpl.wrapTag(rawTag.remove(index))
    override fun isEmpty(): Boolean = rawTag.isEmpty
    override fun getCompound(index: Int): CompoundTag = rawTag.get(index).wrap() as CompoundTag
    override fun getInt(index: Int): Int = rawTag.c(index)
    override fun getIntArray(index: Int): IntArray = rawTag.d(index)
    override fun getDouble(index: Int): Double = rawTag.f(index)
    override fun getFloat(index: Int): Float = rawTag.g(index)
    override fun getString(index: Int): String = rawTag.getString(index)
    override operator fun get(index: Int): Tag<*> = rawTag.i(index).wrap()

    override val size: Int
        get() = rawTag.size()

    override val elementType: TagType<*>
        get() = TagType.ofId(rawTag.g()) ?: throw IllegalStateException("Unknown tag id: ${rawTag.g()}")
}

data class CompoundTagImpl(override val rawTag: NBTTagCompound) : CompoundTag {
    override val value: Map<String, Tag<*>>
        get() = rawTag.c().associateWith { rawTag[it].wrap() }

    override fun copy(): CompoundTag = CompoundTagImpl(rawTag.g())

    override fun getAllKeys(): Set<String> = rawTag.c()
    override val size: Int = rawTag.d()

    override operator fun set(key: String, value: Tag<*>) = rawTag.set(key, value.rawTag as NBTBase)
    override fun putByte(key: String, value: Byte) = rawTag.setByte(key, value)
    override fun putShort(key: String, value: Short) = rawTag.setShort(key, value)
    override fun putInt(key: String, value: Int) = rawTag.setInt(key, value)
    override fun putLong(key: String, value: Long) = rawTag.setLong(key, value)
    override fun putUUID(key: String, value: UUID) = rawTag.a(key, value)
    override fun putFloat(key: String, value: Float) = rawTag.setFloat(key, value)
    override fun putDouble(key: String, value: Double) = rawTag.setDouble(key, value)
    override fun putString(key: String, value: String) = rawTag.setString(key, value)
    override fun putByteArray(key: String, value: ByteArray) = rawTag.setByteArray(key, value)
    override fun putIntArray(key: String, value: IntArray) = rawTag.setIntArray(key, value)
    override fun putIntArray(key: String, value: List<Int>) = rawTag.setIntArray(key, value.toIntArray())
    override fun putLongArray(key: String, value: LongArray) = rawTag.set(key, NBTTagLongArray(value))
    override fun putLongArray(key: String, value: List<Long>) = rawTag.set(key, NBTTagLongArray(value))
    override fun putBoolean(key: String, value: Boolean) = rawTag.setBoolean(key, value)

    override fun getTagType(key: String): TagType<*> = TagType.ofId(rawTag.d(key).toInt())
        ?: throw IllegalStateException("Unknown tag id: ${rawTag.g()}")

    override operator fun contains(key: String): Boolean = rawTag.hasKey(key)
    override fun contains(key: String, type: TagType<*>): Boolean = rawTag.hasKeyOfType(key, type.id)
    override fun hasUUID(key: String): Boolean = rawTag.b(key)

    override fun get(key: String): Tag<*> = rawTag.get(key).wrap()
    override fun getByte(key: String): Byte = rawTag.getByte(key)
    override fun getShort(key: String): Short = rawTag.getShort(key)
    override fun getInt(key: String): Int = rawTag.getInt(key)
    override fun getLong(key: String): Long = rawTag.getLong(key)
    override fun getUUID(key: String): UUID = rawTag.a(key)!!
    override fun getFloat(key: String): Float = rawTag.getFloat(key)
    override fun getDouble(key: String): Double = rawTag.getDouble(key)
    override fun getString(key: String): String = rawTag.getString(key)
    override fun getByteArray(key: String): ByteArray = rawTag.getByteArray(key)
    override fun getIntArray(key: String): IntArray = rawTag.getIntArray(key)
    override fun getLongArray(key: String): LongArray = (rawTag.get(key) as? NBTTagLongArray)?.value ?: longArrayOf()
    override fun getCompound(key: String): CompoundTag = rawTag.getCompound(key).wrap() as CompoundTag
    override fun getList(key: String, type: TagType<*>): ListTag = rawTag.getList(key, type.id).wrap() as ListTag
    override fun getBoolean(key: String): Boolean = rawTag.getBoolean(key)

    override fun remove(key: String) = rawTag.remove(key)
    override fun isEmpty(): Boolean = rawTag.isEmpty
    override fun merge(compound: CompoundTag) = rawTag.a(compound.rawTag as NBTTagCompound)
}

data class IntArrayTagImpl(override val rawTag: NBTTagIntArray) : IntArrayTag {
    override val value: IntArray
        get() = rawTag.d()

    override fun copy(): IntArrayTag = IntArrayTagImpl(rawTag.c())

    override fun isEmpty(): Boolean = rawTag.isEmpty
}

data class LongArrayTagImpl(override val rawTag: NBTTagLongArray) : LongArrayTag {
    override val value: LongArray
        get() = rawTag.value

    override fun copy(): LongArrayTag = LongArrayTagImpl(rawTag.c())

    override fun isEmpty(): Boolean = rawTag.isEmpty
}