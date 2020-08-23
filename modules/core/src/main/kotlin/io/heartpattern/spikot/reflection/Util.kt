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
@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "RemoveRedundantQualifierName")

package io.heartpattern.spikot.reflection

import java.lang.reflect.AccessibleObject
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.isAccessible

/**
 * Java wrapper type of [Boolean]
 */
public typealias BoxedBoolean = java.lang.Boolean

/**
 * Java wrapper type of [Char]
 */
public typealias BoxedChar = java.lang.Character

/**
 * Java wrapper type of [Byte]
 */
public typealias BoxedByte = java.lang.Byte

/**
 * Java wrapper type of [Short]
 */
public typealias BoxedShort = java.lang.Short

/**
 * Java wrapper type of [Int]
 */
public typealias BoxedInteger = java.lang.Integer

/**
 * Java wrapper type of [Long]
 */
public typealias BoxedLong = java.lang.Long

/**
 * Java wrapper type of [Float]
 */
public typealias BoxedFloat = java.lang.Float

/**
 * Java wrapper type of [Double]
 */
public typealias BoxedDouble = java.lang.Double

/**
 * Return primitive type of [this] if [this] is wrapper type. Otherwise return [this]
 */
public fun KClass<*>.toPrimitive(): KClass<*> {
    return when (this) {
        BoxedBoolean::class -> Boolean::class
        BoxedChar::class -> Char::class
        BoxedByte::class -> Byte::class
        BoxedShort::class -> Short::class
        BoxedInteger::class -> Int::class
        BoxedLong::class -> Long::class
        BoxedFloat::class -> Float::class
        BoxedDouble::class -> Double::class
        else -> this
    }
}

/**
 * Acquire accessibility to [this], run [block], restore original accessibility.
 */
@OptIn(ExperimentalContracts::class)
public inline fun <T> AccessibleObject.withAccessibility(block: () -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    val original = isAccessible
    isAccessible = true
    return try {
        block()
    } finally {
        isAccessible = original
    }
}

/**
 * Acquire accessibility to [this], run [block], restore original accessibility.
 */
@OptIn(ExperimentalContracts::class)
public inline fun <T> KCallable<*>.withAccessibility(block: () -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    val original = isAccessible
    isAccessible = true
    return try {
        block()
    } finally {
        isAccessible = original
    }
}

public val KClass<*>.isAnnotation: Boolean
    get() = isSubclassOf(Annotation::class)