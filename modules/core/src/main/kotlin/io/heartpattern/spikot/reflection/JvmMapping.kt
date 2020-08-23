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

import java.lang.reflect.Method
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaMethod

/**
 * Returns a [KProperty.Getter] instance corresponding to the given Java [Method] instance,
 * or null if this method cannot be represented by a Kotlin getter.
 */
public val Method.kotlinGetter: KProperty.Getter<*>?
    get() = (declaringClass.kotlin.members).asSequence()
        .filterIsInstance<KProperty<*>>()
        .map { it.getter }
        .find { it.javaMethod == this }

/**
 * Returns a [KMutableProperty.Setter] instance corresponding to the given Java [Method] instance,
 * or null if this method cannot be represented by a Kotlin setter.
 */
public val Method.kotlinSetter: KMutableProperty.Setter<*>?
    get() = (declaringClass.kotlin.members).asSequence()
        .filterIsInstance<KMutableProperty<*>>()
        .map { it.setter }
        .find { it.javaMethod == this }