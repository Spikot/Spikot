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

package io.heartpattern.spikot.item

import io.heartpattern.spikot.nbt.CompoundTag
import org.bukkit.inventory.ItemStack

/**
 * Ensure nms backed ItemStack
 */
public class NMSItemStack private constructor(
    public val bukkitStack: ItemStack
) {
    public var tag: CompoundTag?
        get() = NMSItemStackAdapter.getTag(bukkitStack)
        set(value) {
            NMSItemStackAdapter.setTag(bukkitStack, value)
        }

    public fun hasTag(): Boolean {
        return NMSItemStackAdapter.hasTag(bukkitStack)
    }

    public fun saveToTag(): CompoundTag {
        return NMSItemStackAdapter.saveToTag(bukkitStack)
    }

    public companion object {
        public fun asMirror(item: ItemStack): NMSItemStack? {
            return if (NMSItemStackAdapter.isNMSCraftItemStack(item))
                NMSItemStack(item)
            else
                null
        }

        public fun asCopy(item: ItemStack): NMSItemStack {
            return NMSItemStack(NMSItemStackAdapter.toCraftItemStack(item))
        }

        public fun asMirrorOrCopy(item: ItemStack): NMSItemStack {
            return if (NMSItemStackAdapter.isNMSCraftItemStack(item))
                NMSItemStack(item)
            else
                NMSItemStack(NMSItemStackAdapter.toCraftItemStack(item))
        }

        public fun loadTag(tag: CompoundTag): NMSItemStack {
            return NMSItemStack(NMSItemStackAdapter.loadFromTag(tag))
        }
    }
}