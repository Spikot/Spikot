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

package io.heartpattern.spikot.type

public abstract class RestrictedKeyMap<K, V> : Map<K, V> {
    private val delegate = HashMap<K, V>()

    abstract override val keys: Set<K>
    public abstract fun default(key: K): V

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