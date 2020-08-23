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

import kotlin.reflect.KClass

/**
 * Describe injection point bean description
 */
public class BeanDescription private constructor(
    public val type: KClass<*>?,
    public val name: String?
) {
    public companion object {
        public fun fromType(type: KClass<*>): BeanDescription {
            return BeanDescription(type, null)
        }

        public fun fromName(name: String): BeanDescription {
            return BeanDescription(null, name)
        }

        public fun fromTypeAndName(type: KClass<*>, name: String?): BeanDescription {
            return BeanDescription(type, name)
        }
    }

    internal fun getDeterminedBeanName(): String {
        return name
            ?: type?.resolveBeanName()
            ?: throw IllegalArgumentException("Cannot resolve bean name")
    }

    public operator fun component1(): KClass<*>? {
        return type
    }

    public operator fun component2(): String? {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BeanDescription) return false

        if (type != other.type) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "BeanDescription(type=$type, name=$name)"
    }
}