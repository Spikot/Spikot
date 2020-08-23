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

package io.heartpattern.spikot.util

import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.Metadatable
import org.bukkit.plugin.Plugin
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Metadata extension property delegate of type [T] with [key] and owing plugin [this]
 */
public inline fun <reified T : Any> Plugin.metadataDelegate(key: String): ReadWriteProperty<Metadatable, T?> {
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
public fun Plugin.booleanMetadataDelegate(key: String): ReadWriteProperty<Metadatable, Boolean> {
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