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

package io.heartpattern.spikot.reflection

import java.lang.reflect.Field
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmName

/**
 * Get field of [R] named [name] with return type [T]]
 * @throws IllegalArgumentException If field does not exists or return type mismatch
 */
public inline fun <reified R : Any, reified T> fieldOf(name: String): Field {
    val field = R::class.toPrimitive().java.getDeclaredField(name)
        ?: throw IllegalArgumentException("Field not found: ${R::class.jvmName}::${name}")
    if (field.type != T::class.toPrimitive().java)
        throw IllegalArgumentException("Return type mismatch: "
            + "expect ${T::class.toPrimitive().java.name}, but ${field.type}")

    return field
}

/**
 * Get [FieldAccess] representing field of [R] named [name] with return type [T]
 * @throws IllegalArgumentException If field does not exists or return type mismatch
 */
public inline fun <reified R : Any, reified T> fieldDelegate(name: String): FieldAccess<R, T> {
    return FieldAccess(fieldOf<R, T>(name))
}

/**
 * Delegate property to access field with receiver [R] and return type [T]
 */
public class FieldAccess<R : Any, T> @PublishedApi internal constructor(
    private val field: Field
) : ReadWriteProperty<R, T> {
    init {
        field.isAccessible = true
    }

    override fun getValue(thisRef: R, property: KProperty<*>): T {
        @Suppress("UNCHECKED_CAST")
        return field.get(thisRef) as T
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
        field.set(thisRef, value)
    }
}