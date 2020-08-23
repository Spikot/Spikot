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

package io.heartpattern.spikot.extension

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

/**
 * Convert nullable property delegate to nonnull property delegate
 */
public fun <R, T> ReadOnlyProperty<R, T?>.nonnull(): ReadOnlyProperty<R, T> {
    return ReadOnlyProperty<R, T> { thisRef, property -> this@nonnull.getValue(thisRef, property)!! }
}

/**
 * Convert nullable property delegate to nonnull property delegate
 */
public fun <R, T> ReadWriteProperty<R, T?>.nonnull(): ReadWriteProperty<R, T> {
    return object : ReadWriteProperty<R, T> {
        override fun getValue(thisRef: R, property: KProperty<*>): T {
            return this@nonnull.getValue(thisRef, property)!!
        }

        override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
            return this@nonnull.setValue(thisRef, property, value)
        }
    }
}

public operator fun <R> KMutableProperty0<R>.getValue(thisRef: Any?, property: KProperty<*>): R {
    return this.get()
}

public operator fun <R> KMutableProperty0<R>.setValue(thisRef: Any?, property: KProperty<*>, value: R) {
    this.set(value)
}
