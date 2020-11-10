/*
 * Copyright (c) 2020 HeartPattern and Spikot authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.heartpattern.spikot.nbt

import java.util.*

public interface Tag<T : Any> {
    public val rawTag: Any
    public val type: TagType<T>
    public val value: T

    public fun isEmpty(): Boolean = false
    public fun copy(): Tag<T>
}

public interface NumericTag<T : Number> : Tag<T> {
    public val long: Long
        get() = value.toLong()
    public val int: Int
        get() = value.toInt()
    public val short: Short
        get() = value.toShort()
    public val byte: Byte
        get() = value.toByte()
    public val double: Double
        get() = value.toDouble()
    public val float: Float
        get() = value.toFloat()
}

public interface EndTag : Tag<Unit> {
    override val type: TagType<Unit>
        get() = TagType.END

    override fun copy(): EndTag
}

public interface ByteTag : NumericTag<Byte> {
    override val type: TagType<Byte>
        get() = TagType.BYTE

    override fun copy(): ByteTag
}

public interface ShortTag : NumericTag<Short> {
    override val type: TagType<Short>
        get() = TagType.SHORT

    override fun copy(): ShortTag
}

public interface IntTag : NumericTag<Int> {

    override val type: TagType<Int>
        get() = TagType.INT

    override fun copy(): IntTag

}

public interface LongTag : NumericTag<Long> {
    override val type: TagType<Long>
        get() = TagType.LONG

    override fun copy(): LongTag

}

public interface FloatTag : NumericTag<Float> {
    override val type: TagType<Float>
        get() = TagType.FLOAT

    override fun copy(): FloatTag
}

public interface DoubleTag : NumericTag<Double> {
    override val type: TagType<Double>
        get() = TagType.DOUBLE

    override fun copy(): DoubleTag
}

public interface ByteArrayTag : Tag<ByteArray> {
    override val type: TagType<ByteArray>
        get() = TagType.BYTE_ARRAY

    override fun copy(): ByteArrayTag
}

public interface StringTag : Tag<String> {
    public override val type: TagType<String>
        get() = TagType.STRING

    public override fun copy(): StringTag
}

public interface ListTag : Tag<List<Tag<*>>> {
    public override val type: TagType<List<Tag<*>>>
        get() = TagType.LIST

    override fun copy(): ListTag

    override fun isEmpty(): Boolean = size == 0
    public operator fun get(index: Int): Tag<*>
    public fun add(element: Tag<*>)
    public fun add(index: Int, element: Tag<*>)
    public fun remove(index: Int): Tag<*>

    public fun getCompound(index: Int): CompoundTag
    public fun getInt(index: Int): Int
    public fun getIntArray(index: Int): IntArray
    public fun getDouble(index: Int): Double
    public fun getFloat(index: Int): Float
    public fun getString(index: Int): String

    public val elementType: TagType<*>
    public val size: Int
}

public interface CompoundTag : Tag<Map<String, Tag<*>>> {
    public override val type: TagType<Map<String, Tag<*>>>
        get() = TagType.COMPOUND

    public override fun copy(): CompoundTag

    public fun getAllKeys(): Set<String>
    public val size: Int
    public operator fun set(key: String, value: Tag<*>)
    public fun putByte(key: String, value: Byte)
    public fun putShort(key: String, value: Short)
    public fun putInt(key: String, value: Int)
    public fun putLong(key: String, value: Long)
    public fun putUUID(key: String, value: UUID)
    public fun putFloat(key: String, value: Float)
    public fun putDouble(key: String, value: Double)
    public fun putString(key: String, value: String)
    public fun putByteArray(key: String, value: ByteArray)
    public fun putIntArray(key: String, value: IntArray)
    public fun putIntArray(key: String, value: List<Int>)
    public fun putLongArray(key: String, value: LongArray)
    public fun putLongArray(key: String, value: List<Long>)
    public fun putBoolean(key: String, value: Boolean)

    public fun getTagType(key: String): TagType<*>
    public operator fun contains(key: String): Boolean
    public fun contains(key: String, type: TagType<*>): Boolean
    public fun hasUUID(key: String): Boolean

    public fun get(key: String): Tag<*>
    public fun getByte(key: String): Byte
    public fun getShort(key: String): Short
    public fun getInt(key: String): Int
    public fun getLong(key: String): Long
    public fun getUUID(key: String): UUID
    public fun getFloat(key: String): Float
    public fun getDouble(key: String): Double
    public fun getString(key: String): String
    public fun getByteArray(key: String): ByteArray
    public fun getIntArray(key: String): IntArray
    public fun getLongArray(key: String): LongArray
    public fun getCompound(key: String): CompoundTag
    public fun getList(key: String, type: TagType<*>): ListTag
    public fun getBoolean(key: String): Boolean

    public fun remove(key: String)
    override fun isEmpty(): Boolean
    public fun merge(compound: CompoundTag)
}

public interface IntArrayTag : Tag<IntArray> {
    override val type: TagType<IntArray>
        get() = TagType.INT_ARRAY

    override fun copy(): IntArrayTag
}

public interface LongArrayTag : Tag<LongArray> {
    override val type: TagType<LongArray>
        get() = TagType.LONG_ARRAY

    override fun copy(): LongArrayTag
}