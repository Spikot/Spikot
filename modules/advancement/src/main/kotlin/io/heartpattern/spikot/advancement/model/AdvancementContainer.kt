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

package io.heartpattern.spikot.advancement.model

import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import io.heartpattern.spikot.ResourceLocation

public class AdvancementContainer{
    private val container = HashMap<ResourceLocation, Advancement>()
    @Suppress("UnstableApiUsage")
    private val parent: Multimap<ResourceLocation?, Advancement> = MultimapBuilder.hashKeys().linkedListValues().build()

    public val allAdvancements: Collection<Advancement>
        get() = container.values

    public val root: Advancement?
        get() = parent[null].single()

    public fun addAdvancement(advancement: Advancement){
        container[advancement.id] = advancement
    }

    public operator fun get(id: ResourceLocation): Advancement?{
        return container[id]
    }

    public fun getChildren(id: ResourceLocation): Collection<Advancement>{
        return parent[id]
    }

    public fun getChildren(parent: Advancement): Collection<Advancement>{
        return this.parent[parent.id]
    }

    public companion object{
        public fun of(vararg advancement: Advancement): AdvancementContainer{
            return AdvancementContainer().apply{
                advancement.forEach(this::addAdvancement)
            }
        }

        public fun of(advancement: Iterable<Advancement>): AdvancementContainer{
            return AdvancementContainer().apply{
                advancement.forEach(this::addAdvancement)
            }
        }
    }
}