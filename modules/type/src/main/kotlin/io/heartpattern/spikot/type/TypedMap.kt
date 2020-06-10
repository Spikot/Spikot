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

package io.heartpattern.spikot.type

/**
 * Key of [TypedMap]
 */
open class Attribute<T>

/**
 * Key of [TypedMap] with default value
 */
abstract class DefaultTypedKey<T> : Attribute<T>() {
    abstract val default: T
}

@Suppress("FunctionName")
fun <T> DefaultTypedKey(default: T): DefaultTypedKey<T> = object : DefaultTypedKey<T>() {
    override val default: T
        get() = default
}

/**
 * Map that store typed key-value
 */
open class TypedMap(
    protected val backingMap: MutableMap<Attribute<Any?>, Value<Any?>> = HashMap()
) {
    operator fun <T> get(key: Attribute<T>): Option<T> {
        @Suppress("USELESS_CAST")
        val value = backingMap[key as Attribute<*>]

        @Suppress("UNCHECKED_CAST")
        return if (value == null)
            None
        else
            Some(value as T)
    }

    operator fun <T> get(key: DefaultTypedKey<T>): T {
        @Suppress("UNCHECKED_CAST")
        val value = backingMap.getOrPut(key as Attribute<Any?>) {
            Value(key.default)
        }

        @Suppress("UNCHECKED_CAST")
        return value.value as T
    }

    operator fun <T> set(key: Attribute<T>, value: T) {
        @Suppress("UNCHECKED_CAST")
        backingMap[key as Attribute<Any?>] = Value(value)
    }

    operator fun contains(key: Attribute<*>): Boolean {
        return backingMap.containsKey(key)
    }

    fun <T> remove(key: Attribute<T>): Option<T> {
        val removed = backingMap.remove(key as Attribute<*>)

        @Suppress("UNCHECKED_CAST")
        return if (removed != null)
            Some(removed.value as T)
        else
            None
    }

    fun isEmpty(): Boolean {
        return backingMap.isEmpty()
    }

    val keys: Set<Attribute<*>>
        get() = backingMap.keys
}