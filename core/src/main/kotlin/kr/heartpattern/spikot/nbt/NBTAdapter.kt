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

package kr.heartpattern.spikot.nbt

import kr.heartpattern.spikot.adapter.getAdapterOf

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