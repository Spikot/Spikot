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

package io.heartpattern.spikot.type

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Map that store typed key-value
 */
public open class TypedMap(
    protected val backingMap: MutableMap<Key<Any?>, Value<Any?>> = HashMap()
) {
    public operator fun <T> get(key: Key<T>): Option<T> {
        @Suppress("USELESS_CAST")
        val value = backingMap[key as Key<*>]

        @Suppress("UNCHECKED_CAST")
        return if (value == null)
            None
        else
            Some(value.value as T)
    }

    public operator fun <T> get(key: DefaultTypedKey<T>): T {
        @Suppress("UNCHECKED_CAST")
        val value = backingMap.getOrPut(key as Key<Any?>) {
            Value(key.default)
        }

        @Suppress("UNCHECKED_CAST")
        return value.value as T
    }

    public operator fun <T> set(key: Key<T>, value: T) {
        @Suppress("UNCHECKED_CAST")
        backingMap[key as Key<Any?>] = Value(value)
    }

    @OptIn(ExperimentalContracts::class)
    public inline fun <T> getOrCompute(key: Key<T>, compute: () -> T): T {
        contract { callsInPlace(compute, InvocationKind.AT_MOST_ONCE) }
        val value = this[key]
        if (value is Some)
            return value.value

        val computed = compute()
        this[key] = computed
        return computed
    }

    public operator fun contains(key: Key<*>): Boolean {
        return backingMap.containsKey(key)
    }

    public fun <T> remove(key: Key<T>): Option<T> {
        val removed = backingMap.remove(key as Key<*>)

        @Suppress("UNCHECKED_CAST")
        return if (removed != null)
            Some(removed.value as T)
        else
            None
    }

    public fun isEmpty(): Boolean {
        return backingMap.isEmpty()
    }

    public val keys: Set<Key<*>>
        get() = backingMap.keys

    /**
     * Key of [TypedMap]
     */
    public open class Key<T>

    /**
     * Key of [TypedMap] with default value
     */
    public abstract class DefaultTypedKey<T> : Key<T>() {
        public abstract val default: T
    }

    public companion object {
        @Suppress("FunctionName")
        public fun <T> DefaultTypedKey(default: T): DefaultTypedKey<T> = object : DefaultTypedKey<T>() {
            override val default: T
                get() = default
        }
    }
}