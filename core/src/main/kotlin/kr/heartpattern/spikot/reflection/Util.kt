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
@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "RemoveRedundantQualifierName")

package kr.heartpattern.spikot.reflection

import java.lang.reflect.AccessibleObject
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.jvm.isAccessible

/**
 * Java wrapper type of [Boolean]
 */
typealias BoxedBoolean = java.lang.Boolean

/**
 * Java wrapper type of [Char]
 */
typealias BoxedChar = java.lang.Character

/**
 * Java wrapper type of [Byte]
 */
typealias BoxedByte = java.lang.Byte

/**
 * Java wrapper type of [Short]
 */
typealias BoxedShort = java.lang.Short

/**
 * Java wrapper type of [Int]
 */
typealias BoxedInteger = java.lang.Integer

/**
 * Java wrapper type of [Long]
 */
typealias BoxedLong = java.lang.Long

/**
 * Java wrapper type of [Float]
 */
typealias BoxedFloat = java.lang.Float

/**
 * Java wrapper type of [Double]
 */
typealias BoxedDouble = java.lang.Double

/**
 * Return primitive type of [this] if [this] is wrapper type. Otherwise return [this]
 */
fun KClass<*>.toPrimitive(): KClass<*> {
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
 * Get annotation [T] annotating [this]
 */
inline fun <reified T : Annotation> KClass<*>.getAnnotation(): T? {
    return annotations.filterIsInstance<T>().firstOrNull()
}

/**
 * Set accessible of [this] true, run [block], restore original accessibility.
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> AccessibleObject.withAccessibility(block: () -> T): T {
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
 * Set accessible of [this], run [block], restore original accessibility.
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> KCallable<*>.withAccessibility(block: () -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    val original = isAccessible
    isAccessible = true
    return try {
        block()
    } finally {
        isAccessible = original
    }
}