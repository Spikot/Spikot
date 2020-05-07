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

package kr.heartpattern.spikot.reflection

import java.lang.reflect.Field
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmName

/**
 * Get field of [R] named [name] with return type [T]]
 * @throws IllegalArgumentException If field does not exists or return type mismatch
 */
inline fun <reified R : Any, reified T> fieldOf(name: String): Field {
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
inline fun <reified R : Any, reified T> fieldDelegate(name: String): FieldAccess<R, T> {
    return FieldAccess(fieldOf<R, T>(name))
}

/**
 * Delegate property to access field with receiver [R] and return type [T]
 */
class FieldAccess<R : Any, T> @PublishedApi internal constructor(
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