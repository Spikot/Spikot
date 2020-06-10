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

import kotlin.reflect.KProperty

/**
 * Simply holding [value]
 */
abstract class Value<out T> internal constructor() {
    abstract val value: T

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }
}

/**
 * Simply holding mutable [value]
 */
abstract class MutableValue<T> internal constructor() {
    abstract var value: T

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

@Suppress("FunctionName")
fun <T> Value(value: T): Value<T> = object : Value<T>() {
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
fun <T> MutableValue(value: T): MutableValue<T> = object : MutableValue<T>() {
    override var value: T = value

    override fun equals(other: Any?): Boolean = when (other) {
        null -> false
        !is MutableValue<*> -> false
        else -> other.value == value
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()
}