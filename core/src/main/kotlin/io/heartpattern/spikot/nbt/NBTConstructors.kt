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

@file:Suppress("FunctionName")

package io.heartpattern.spikot.nbt

fun EndTag(): EndTag = NBTAdapter.createEndTag()
fun ByteTag(value: Byte): ByteTag = NBTAdapter.createByteTag(value)
fun ShortTag(value: Short): ShortTag = NBTAdapter.createShortTag(value)
fun IntTag(value: Int): IntTag = NBTAdapter.createIntTag(value)
fun LongTag(value: Long): LongTag = NBTAdapter.createLongTag(value)
fun FloatTag(value: Float): FloatTag = NBTAdapter.createFloatTag(value)
fun DoubleTag(value: Double): DoubleTag = NBTAdapter.createDoubleTag(value)
fun ByteArrayTag(value: ByteArray): ByteArrayTag = NBTAdapter.createByteArrayTag(value)
fun ByteArrayTag(value: List<Byte>): ByteArrayTag = NBTAdapter.createByteArrayTag(value)
fun StringTag(): StringTag = NBTAdapter.createStringTag()
fun StringTag(value: String): StringTag = NBTAdapter.createStringTag(value)
fun ListTag(): ListTag = NBTAdapter.createListTag()
fun CompoundTag(): CompoundTag = NBTAdapter.createCompoundTag()
fun IntArrayTag(value: IntArray): IntArrayTag = NBTAdapter.createIntArrayTag(value)
fun IntArrayTag(value: List<Int>): IntArrayTag = NBTAdapter.createIntArrayTag(value)
fun LongArrayTag(value: LongArray): LongArrayTag = NBTAdapter.createLongArrayTag(value)
fun LongArrayTag(value: List<Long>): LongArrayTag = NBTAdapter.createLongArrayTag(value)
