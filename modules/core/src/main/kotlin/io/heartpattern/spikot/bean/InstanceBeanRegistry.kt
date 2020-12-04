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

package io.heartpattern.spikot.bean

public class InstanceBeanRegistry(
    private val parent: BeanRegistry,
    private val registry: Map<String, BeanHolder>
) : BeanRegistry {
    override val allBeanName: Collection<String> = registry.keys + parent.allBeanName

    override val allBeanProcessors: Collection<String> = registry.asSequence()
        .filter { (_, v) -> v.instance is BeanProcessor }
        .map { it.key }
        .toList() + parent.allBeanProcessors

    override fun getBean(description: BeanDescription): Any {
        val list = getBeanHolders(description)
        if (list.isEmpty())
            return parent.getBean(description)

        if (list.size >= 2 && list.count { it.isPrimary } != 1)
            throw IllegalArgumentException("Cannot select unique bean from $description")

        return list.first().instance
    }

    override fun getBeans(description: BeanDescription): List<Any> {
        return getBeanHolders(description).map { it.instance } + parent.getBeans(description)
    }

    override fun hasBean(description: BeanDescription): Boolean {
        return getBeanHolders(description).isNotEmpty() || parent.hasBean(description)
    }

    private fun getBeanHolders(description: BeanDescription): List<BeanHolder> {
        val (type, name) = description

        return if (name != null) {
            val namedBean = registry[name]
            if (namedBean == null || (type != null && !type.isInstance(namedBean.instance))) emptyList()
            else listOf(namedBean)
        } else {
            type!!
            registry.values.filter { type.isInstance(it.instance) }
        }
    }

    override fun hasInitializedBean(description: BeanDescription): Boolean {
        return hasBean(description) || parent.hasInitializedBean(description)
    }

    public data class BeanHolder(val instance: Any, val isPrimary: Boolean = false, val priority: Int = 0)
}