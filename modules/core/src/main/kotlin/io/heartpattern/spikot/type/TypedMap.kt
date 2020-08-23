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

/**
 * Key of [TypedMap]
 */
public open class Attribute<T>

/**
 * Key of [TypedMap] with default value
 */
public abstract class DefaultTypedKey<T> : Attribute<T>() {
    public abstract val default: T
}

@Suppress("FunctionName")
public fun <T> DefaultTypedKey(default: T): DefaultTypedKey<T> = object : DefaultTypedKey<T>() {
    override val default: T
        get() = default
}

/**
 * Map that store typed key-value
 */
public open class TypedMap(
    protected val backingMap: MutableMap<Attribute<Any?>, Value<Any?>> = HashMap()
) {
    public operator fun <T> get(key: Attribute<T>): Option<T> {
        @Suppress("USELESS_CAST")
        val value = backingMap[key as Attribute<*>]

        @Suppress("UNCHECKED_CAST")
        return if (value == null)
            None
        else
            Some(value as T)
    }

    public operator fun <T> get(key: DefaultTypedKey<T>): T {
        @Suppress("UNCHECKED_CAST")
        val value = backingMap.getOrPut(key as Attribute<Any?>) {
            Value(key.default)
        }

        @Suppress("UNCHECKED_CAST")
        return value.value as T
    }

    public operator fun <T> set(key: Attribute<T>, value: T) {
        @Suppress("UNCHECKED_CAST")
        backingMap[key as Attribute<Any?>] = Value(value)
    }

    public operator fun contains(key: Attribute<*>): Boolean {
        return backingMap.containsKey(key)
    }

    public fun <T> remove(key: Attribute<T>): Option<T> {
        val removed = backingMap.remove(key as Attribute<*>)

        @Suppress("UNCHECKED_CAST")
        return if (removed != null)
            Some(removed.value as T)
        else
            None
    }

    public fun isEmpty(): Boolean {
        return backingMap.isEmpty()
    }

    public val keys: Set<Attribute<*>>
        get() = backingMap.keys
}