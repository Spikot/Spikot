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

abstract class RestrictedKeyMap<K, V> : Map<K, V> {
    private val delegate = HashMap<K, V>()

    abstract override val keys: Set<K>
    abstract fun default(key: K): V

    private fun synchronize() {
        val allKeys = keys

        val iter = delegate.iterator()
        while (iter.hasNext())
            if (iter.next().key in allKeys)
                iter.remove()

        for (key in keys)
            if (!delegate.containsKey(key))
                delegate[key] = default(key)
    }

    override val entries: Set<Map.Entry<K, V>>
        get() {
            synchronize()
            return delegate.entries
        }
    override val size: Int
        get() = keys.size
    override val values: Collection<V>
        get() {
            synchronize()
            return delegate.values
        }

    override fun containsKey(key: K): Boolean = key in keys

    override fun containsValue(value: V): Boolean {
        synchronize()
        return delegate.containsValue(value)
    }

    override fun get(key: K): V? {
        if (key !in keys) return null
        if (key in delegate) return delegate[key]
        return delegate.put(key, default(key))
    }

    override fun isEmpty(): Boolean = keys.isEmpty()
}