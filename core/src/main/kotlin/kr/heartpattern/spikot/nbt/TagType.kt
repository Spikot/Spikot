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

import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
sealed class TagType<T : Any>(val id: Int, val type: KClass<T>, val name: String) {
    object END : TagType<Unit>(0, Unit::class, "TAG_End")
    object BYTE : TagType<Byte>(1, Byte::class, "TAG_Byte")
    object SHORT : TagType<Short>(2, Short::class, "TAG_Short")
    object INT : TagType<Int>(3, Int::class, "TAG_Int")
    object LONG : TagType<Long>(4, Long::class, "TAG_Long")
    object FLOAT : TagType<Float>(5, Float::class, "TAG_Float")
    object DOUBLE : TagType<Double>(6, Double::class, "TAG_Double")
    object BYTE_ARRAY : TagType<ByteArray>(7, ByteArray::class, "TAG_Byte_Array")
    object STRING : TagType<String>(8, String::class, "TAG_String")
    object LIST : TagType<List<Tag<*>>>(9, List::class as KClass<List<Tag<*>>>, "TAG_List")
    object COMPOUND : TagType<Map<String, Tag<*>>>(10, Map::class as KClass<Map<String, Tag<*>>>, "TAG_Compound")
    object INT_ARRAY : TagType<IntArray>(11, IntArray::class, "TAG_Int_Array")
    object LONG_ARRAY : TagType<LongArray>(12, LongArray::class, "TAG_Long_Array")
    object MISC : TagType<Any>(99, Any::class, "AnyNumericTag")

    fun create(): Tag<T> {
        return NBTAdapter.createTag(this)
    }

    companion object {
        val values = listOf(
            END,
            BYTE,
            SHORT,
            INT,
            LONG,
            FLOAT,
            DOUBLE,
            BYTE_ARRAY,
            STRING,
            LIST,
            COMPOUND,
            INT_ARRAY,
            LONG_ARRAY,
            MISC
        )

        fun ofId(id: Int): TagType<*>? = values.find { it.id == id }

        fun ofName(name: String): TagType<*>? = values.find { it.name == name }

        fun <T : Any> ofType(type: KClass<T>): TagType<T>? = values.find { it.type == type } as TagType<T>
    }
}
