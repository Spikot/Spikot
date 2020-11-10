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

import io.heartpattern.spikot.Spikot
import io.heartpattern.spikot.adapter.adapterOf

public interface NBTAdapter {
    public fun createEndTag(): EndTag
    public fun createByteTag(value: Byte): ByteTag
    public fun createShortTag(value: Short): ShortTag
    public fun createIntTag(value: Int): IntTag
    public fun createLongTag(value: Long): LongTag
    public fun createFloatTag(value: Float): FloatTag
    public fun createDoubleTag(value: Double): DoubleTag
    public fun createByteArrayTag(value: ByteArray): ByteArrayTag
    public fun createByteArrayTag(value: List<Byte>): ByteArrayTag
    public fun createStringTag(): StringTag
    public fun createStringTag(value: String): StringTag
    public fun createListTag(): ListTag
    public fun createCompoundTag(): CompoundTag
    public fun createIntArrayTag(value: IntArray): IntArrayTag
    public fun createIntArrayTag(value: List<Int>): IntArrayTag
    public fun createLongArrayTag(value: LongArray): LongArrayTag
    public fun createLongArrayTag(value: List<Long>): LongArrayTag

    public fun <T : Any> createTag(type: TagType<T>): Tag<T>

    public fun wrapEndTag(tag: Any): EndTag
    public fun wrapByteTag(tag: Any): ByteTag
    public fun wrapShortTag(tag: Any): ShortTag
    public fun wrapIntTag(tag: Any): IntTag
    public fun wrapLongTag(tag: Any): LongTag
    public fun wrapFloatTag(tag: Any): FloatTag
    public fun wrapDoubleTag(tag: Any): DoubleTag
    public fun wrapByteArrayTag(tag: Any): ByteArrayTag
    public fun wrapStringTag(tag: Any): StringTag
    public fun wrapListTag(tag: Any): ListTag
    public fun wrapCompoundTag(tag: Any): CompoundTag
    public fun wrapIntArrayTag(tag: Any): IntArrayTag
    public fun wrapLongArrayTag(tag: Any): LongArrayTag

    public fun wrapTag(tag: Any): Tag<*>

    public companion object Impl : NBTAdapter by adapterOf<Spikot, NBTAdapter>()
}