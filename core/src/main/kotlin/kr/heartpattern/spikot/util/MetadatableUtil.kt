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

package kr.heartpattern.spikot.util

import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.Metadatable
import org.bukkit.plugin.Plugin
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Metadata extension property delegate of type [T] with [key] and owing plugin [this]
 */
inline fun <reified T : Any> Plugin.metadataDelegate(key: String): ReadWriteProperty<Metadatable, T?> {
    return object : ReadWriteProperty<Metadatable, T?> {
        override fun getValue(thisRef: Metadatable, property: KProperty<*>): T? {
            return if (thisRef.hasMetadata(key)) {
                thisRef.getMetadata(key).find { it.owningPlugin === this@metadataDelegate }?.value() as T?
            } else {
                null
            }
        }

        override fun setValue(thisRef: Metadatable, property: KProperty<*>, value: T?) {
            if (value == null) {
                thisRef.removeMetadata(key, this@metadataDelegate)
            } else {
                thisRef.setMetadata(key, FixedMetadataValue(this@metadataDelegate, value))
            }
        }
    }
}

/**
 * Metadata extension property delegate of boolean type with [key] and owing plugin [this]
 */
fun Plugin.booleanMetadataDelegate(key: String): ReadWriteProperty<Metadatable, Boolean> {
    return object : ReadWriteProperty<Metadatable, Boolean> {
        override fun getValue(thisRef: Metadatable, property: KProperty<*>): Boolean {
            return if (thisRef.hasMetadata(key)) {
                thisRef.getMetadata(key).any { it.owningPlugin === this@booleanMetadataDelegate }
            } else {
                false
            }
        }

        override fun setValue(thisRef: Metadatable, property: KProperty<*>, value: Boolean) {
            if (value) {
                thisRef.setMetadata(key, FixedMetadataValue(this@booleanMetadataDelegate, value))
            } else {
                thisRef.removeMetadata(key, this@booleanMetadataDelegate)
            }
        }
    }
}