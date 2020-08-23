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

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KVisibility
import kotlin.reflect.jvm.jvmName

/**
 * Get object instance of [this]. This function also try to find object instance of private class.
 * @throws [IllegalArgumentException] [this] is not an object instance
 */
public fun <T : Any> KClass<T>.getUnsafeObjectInstance(): T {
    return getUnsafeObjectInstanceOrNull()
        ?: throw IllegalArgumentException("$jvmName is not object")
}

/**
 * Get object instance of [this] or null if fail to find object instance.
 * This function also try to find object instance of private class.
 */
@Suppress("UNCHECKED_CAST")
public fun <T : Any> KClass<T>.getUnsafeObjectInstanceOrNull(): T? {
    return if (this.visibility != KVisibility.PUBLIC) {
        try {
            val objectInstanceField = this.java.getDeclaredField("INSTANCE")
            objectInstanceField.withAccessibility {
                objectInstanceField.get(null) as T
            }
        } catch (e: NoSuchFieldException) {
            null
        }
    } else {
        this.objectInstance
    }
}

/**
 * Create new instance of [this]. This function can create instance with private constructor.
 * @throws [IllegalArgumentException] [this] does not has no-arg constructor.
 */
public fun <T : Any> KClass<T>.createUnsafeInstance(): T {
    return createUnsafeInstanceOrNull()
        ?: throw IllegalArgumentException("$jvmName does not have no-arg constructor")
}

/**
 * Create new instance of [this] or null if failed to find no-arg constructor.
 * This function can create instance with private constructor.
 */
public fun <T : Any> KClass<T>.createUnsafeInstanceOrNull(): T? {
    val constructor = constructors.find { it.parameters.all(KParameter::isOptional) }
        ?: return null

    return constructor.withAccessibility {
        constructor.call()
    }
}

/**
 * Create object instance of [this] if [this] is object, or create instance if [this] has no-arg constructor.
 * @throws [IllegalArgumentException] [this] is not object and does not have no-arg constructor
 */
public fun <T : Any> KClass<T>.getObjectInstanceOrCreate(): T {
    return getUnsafeObjectInstanceOrNull()
        ?: createUnsafeInstanceOrNull()
        ?: throw IllegalArgumentException("Cannot find object instance or no-arg constructor of $jvmName")
}