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

import kotlin.reflect.KProperty

/**
 * Simply holding [value]
 */
public abstract class Value<out T> internal constructor() {
    public abstract val value: T

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }
}

/**
 * Simply holding mutable [value]
 */
public abstract class MutableValue<T> internal constructor() {
    public abstract var value: T

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

@Suppress("FunctionName")
public fun <T> Value(value: T): Value<T> = object : Value<T>() {
    override val value: T = value

    override fun equals(other: Any?): Boolean = when (other) {
        null -> false
        !is Value<*> -> false
        else -> other.value == value
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()
}

@Suppress("FunctionName")
public fun <T> MutableValue(value: T): MutableValue<T> = object : MutableValue<T>() {
    override var value: T = value

    override fun equals(other: Any?): Boolean = when (other) {
        null -> false
        !is MutableValue<*> -> false
        else -> other.value == value
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()
}