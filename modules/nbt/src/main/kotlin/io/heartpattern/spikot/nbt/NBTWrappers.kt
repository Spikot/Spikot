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

interface Tag<T : Any> {
    val rawTag: Any
    val type: TagType<T>
    val value: T

    fun isEmpty(): Boolean = false
    fun copy(): Tag<T>
}

interface NumericTag<T : Number> : Tag<T> {
    val long: Long
        get() = value.toLong()
    val int: Int
        get() = value.toInt()
    val short: Short
        get() = value.toShort()
    val byte: Byte
        get() = value.toByte()
    val double: Double
        get() = value.toDouble()
    val float: Float
        get() = value.toFloat()
}

interface EndTag : Tag<Unit> {
    override val type: TagType<Unit>
        get() = TagType.END

    override fun copy(): EndTag
}

interface ByteTag : NumericTag<Byte> {
    override val type: TagType<Byte>
        get() = TagType.BYTE

    override fun copy(): ByteTag
}

interface ShortTag : NumericTag<Short> {
    override val type: TagType<Short>
        get() = TagType.SHORT

    override fun copy(): ShortTag
}

interface IntTag : NumericTag<Int> {

    override val type: TagType<Int>
        get() = TagType.INT

    override fun copy(): IntTag

}

interface LongTag : NumericTag<Long> {
    override val type: TagType<Long>
        get() = TagType.LONG

    override fun copy(): LongTag

}

interface FloatTag : NumericTag<Float> {
    override val type: TagType<Float>
        get() = TagType.FLOAT

    override fun copy(): FloatTag
}

interface DoubleTag : NumericTag<Double> {
    override val type: TagType<Double>
        get() = TagType.DOUBLE

    override fun copy(): DoubleTag
}

interface ByteArrayTag : Tag<ByteArray> {
    override val type: TagType<ByteArray>
        get() = TagType.BYTE_ARRAY

    override fun copy(): ByteArrayTag
}

interface StringTag : Tag<String> {
    override val type: TagType<String>
        get() = TagType.STRING

    override fun copy(): StringTag
}

interface ListTag : Tag<List<Tag<*>>> {
    override val type: TagType<List<Tag<*>>>
        get() = TagType.LIST

    override fun copy(): ListTag

    override fun isEmpty() = size == 0
    operator fun get(index: Int): Tag<*>
    fun add(element: Tag<*>)
    fun add(index: Int, element: Tag<*>)
    fun remove(index: Int): Tag<*>

    fun getCompound(index: Int): CompoundTag
    fun getInt(index: Int): Int
    fun getIntArray(index: Int): IntArray
    fun getDouble(index: Int): Double
    fun getFloat(index: Int): Float
    fun getString(index: Int): String

    val elementType: TagType<*>
    val size: Int
}

interface CompoundTag : Tag<Map<String, Tag<*>>> {
    override val type: TagType<Map<String, Tag<*>>>
        get() = TagType.COMPOUND

    override fun copy(): CompoundTag

    fun getAllKeys(): Set<String>
    val size: Int
    operator fun set(key: String, value: Tag<*>)
    fun putByte(key: String, value: Byte)
    fun putShort(key: String, value: Short)
    fun putInt(key: String, value: Int)
    fun putLong(key: String, value: Long)
    fun putUUID(key: String, value: UUID)
    fun putFloat(key: String, value: Float)
    fun putDouble(key: String, value: Double)
    fun putString(key: String, value: String)
    fun putByteArray(key: String, value: ByteArray)
    fun putIntArray(key: String, value: IntArray)
    fun putIntArray(key: String, value: List<Int>)
    fun putLongArray(key: String, value: LongArray)
    fun putLongArray(key: String, value: List<Long>)
    fun putBoolean(key: String, value: Boolean)

    fun getTagType(key: String): TagType<*>
    operator fun contains(key: String): Boolean
    fun contains(key: String, type: TagType<*>): Boolean
    fun hasUUID(key: String): Boolean

    fun get(key: String): Tag<*>
    fun getByte(key: String): Byte
    fun getShort(key: String): Short
    fun getInt(key: String): Int
    fun getLong(key: String): Long
    fun getUUID(key: String): UUID
    fun getFloat(key: String): Float
    fun getDouble(key: String): Double
    fun getString(key: String): String
    fun getByteArray(key: String): ByteArray
    fun getIntArray(key: String): IntArray
    fun getLongArray(key: String): LongArray
    fun getCompound(key: String): CompoundTag
    fun getList(key: String, type: TagType<*>): ListTag
    fun getBoolean(key: String): Boolean

    fun remove(key: String)
    override fun isEmpty(): Boolean
    fun merge(compound: CompoundTag)
}

interface IntArrayTag : Tag<IntArray> {
    override val type: TagType<IntArray>
        get() = TagType.INT_ARRAY

    override fun copy(): IntArrayTag
}

interface LongArrayTag : Tag<LongArray> {
    override val type: TagType<LongArray>
        get() = TagType.LONG_ARRAY

    override fun copy(): LongArrayTag
}