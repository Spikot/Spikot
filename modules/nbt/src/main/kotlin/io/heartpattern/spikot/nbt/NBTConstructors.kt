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

@file:Suppress("FunctionName")

package io.heartpattern.spikot.nbt

public fun EndTag(): EndTag = NBTAdapter.createEndTag()
public fun ByteTag(value: Byte): ByteTag = NBTAdapter.createByteTag(value)
public fun ShortTag(value: Short): ShortTag = NBTAdapter.createShortTag(value)
public fun IntTag(value: Int): IntTag = NBTAdapter.createIntTag(value)
public fun LongTag(value: Long): LongTag = NBTAdapter.createLongTag(value)
public fun FloatTag(value: Float): FloatTag = NBTAdapter.createFloatTag(value)
public fun DoubleTag(value: Double): DoubleTag = NBTAdapter.createDoubleTag(value)
public fun ByteArrayTag(value: ByteArray): ByteArrayTag = NBTAdapter.createByteArrayTag(value)
public fun ByteArrayTag(value: List<Byte>): ByteArrayTag = NBTAdapter.createByteArrayTag(value)
public fun StringTag(): StringTag = NBTAdapter.createStringTag()
public fun StringTag(value: String): StringTag = NBTAdapter.createStringTag(value)
public fun ListTag(): ListTag = NBTAdapter.createListTag()
public fun CompoundTag(): CompoundTag = NBTAdapter.createCompoundTag()
public fun IntArrayTag(value: IntArray): IntArrayTag = NBTAdapter.createIntArrayTag(value)
public fun IntArrayTag(value: List<Int>): IntArrayTag = NBTAdapter.createIntArrayTag(value)
public fun LongArrayTag(value: LongArray): LongArrayTag = NBTAdapter.createLongArrayTag(value)
public fun LongArrayTag(value: List<Long>): LongArrayTag = NBTAdapter.createLongArrayTag(value)
