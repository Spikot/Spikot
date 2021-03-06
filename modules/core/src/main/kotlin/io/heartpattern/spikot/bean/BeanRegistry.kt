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

public interface BeanRegistry {
    /**
     * Get all bean name accessible from this registry.
     */
    public val allBeanName: Collection<String>

    /**
     * Get all beans name that implement [BeanProcessor]
     */
    public val allBeanProcessors: Collection<String>

    /**
     * Get bean of [description] in this scope or parent scope. Throw [BeanNotFoundException] if bean does not exist
     */
    @Throws(BeanNotFoundException::class)
    public fun getBean(description: BeanDescription): Any

    /**
     * Get all bean of [description] in this scope or parent scope
     */
    public fun getBeans(description: BeanDescription): List<Any>

    /**
     * Check bean of [description] is in this scope or parent scope
     */
    public fun hasBean(description: BeanDescription): Boolean

    /**
     * Check bean of [description] is initialized in this scope or parent scope
     */
    public fun hasInitializedBean(description: BeanDescription): Boolean
}