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
