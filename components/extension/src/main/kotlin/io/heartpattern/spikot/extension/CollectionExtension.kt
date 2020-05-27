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

import kotlin.reflect.KProperty

operator fun <K, V> Map<K, V>.getValue(thisRef: K, property: KProperty<*>): V? {
    return this[thisRef]
}

operator fun <K, V> MutableMap<K, V>.setValue(thisRef: K, property: KProperty<*>, value: V) {
    this[thisRef] = value
}

inline fun <reified T> Iterable<*>.findInstance(): T? {
    val iterator = iterator()
    while (iterator.hasNext()) {
        val item = iterator.next()

        if (item is T)
            return item
    }

    return null
}