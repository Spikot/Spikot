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

package io.heartpattern.spikot.extension

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Convert nullable property delegate to nonnull property delegate
 */
fun <R, T> ReadOnlyProperty<R, T?>.nonnull(): ReadOnlyProperty<R, T> {
    return object : ReadOnlyProperty<R, T> {
        override fun getValue(thisRef: R, property: KProperty<*>): T {
            return this@nonnull.getValue(thisRef, property)!!
        }
    }
}

/**
 * Convert nullable property delegate to nonnull property delegate
 */
fun <R, T> ReadWriteProperty<R, T?>.nonnull(): ReadWriteProperty<R, T> {
    return object : ReadWriteProperty<R, T> {
        override fun getValue(thisRef: R, property: KProperty<*>): T {
            return this@nonnull.getValue(thisRef, property)!!
        }

        override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
            return this@nonnull.setValue(thisRef, property, value)
        }
    }
}