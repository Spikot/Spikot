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

import io.heartpattern.spikot.adapter.getAdapterOf

interface NBTAdapter {
    fun createEndTag(): EndTag
    fun createByteTag(value: Byte): ByteTag
    fun createShortTag(value: Short): ShortTag
    fun createIntTag(value: Int): IntTag
    fun createLongTag(value: Long): LongTag
    fun createFloatTag(value: Float): FloatTag
    fun createDoubleTag(value: Double): DoubleTag
    fun createByteArrayTag(value: ByteArray): ByteArrayTag
    fun createByteArrayTag(value: List<Byte>): ByteArrayTag
    fun createStringTag(): StringTag
    fun createStringTag(value: String): StringTag
    fun createListTag(): ListTag
    fun createCompoundTag(): CompoundTag
    fun createIntArrayTag(value: IntArray): IntArrayTag
    fun createIntArrayTag(value: List<Int>): IntArrayTag
    fun createLongArrayTag(value: LongArray): LongArrayTag
    fun createLongArrayTag(value: List<Long>): LongArrayTag

    fun <T : Any> createTag(type: TagType<T>): Tag<T>

    fun wrapEndTag(tag: Any): EndTag
    fun wrapByteTag(tag: Any): ByteTag
    fun wrapShortTag(tag: Any): ShortTag
    fun wrapIntTag(tag: Any): IntTag
    fun wrapLongTag(tag: Any): LongTag
    fun wrapFloatTag(tag: Any): FloatTag
    fun wrapDoubleTag(tag: Any): DoubleTag
    fun wrapByteArrayTag(tag: Any): ByteArrayTag
    fun wrapStringTag(tag: Any): StringTag
    fun wrapListTag(tag: Any): ListTag
    fun wrapCompoundTag(tag: Any): CompoundTag
    fun wrapIntArrayTag(tag: Any): IntArrayTag
    fun wrapLongArrayTag(tag: Any): LongArrayTag

    fun wrapTag(tag: Any): Tag<*>

    companion object Impl : NBTAdapter by getAdapterOf()
}