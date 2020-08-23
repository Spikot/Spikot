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

package io.heartpattern.spikot.scope

import io.heartpattern.spikot.SpikotPlugin
import io.heartpattern.spikot.bean.BeanRegistry

public interface ScopeInstance : BeanRegistry {
    /**
     * Name of scope
     */
    public val scope: String

    /**
     * Name of this scope instance
     */
    public val name: String

    /**
     * Whether this scope is closed
     */
    public val isClosed: Boolean

    /**
     * Owner plugin of this scope
     */
    public val owingPlugin: SpikotPlugin

    /**
     * Register [scope] as child of this scope. Before closing this scope, child scope's [close] will invoke.
     */
    public fun registerChildScope(scope: ScopeInstance)

    /**
     * Unregister [scope] from child of this scope
     */
    public fun unregisterChildScope(scope: ScopeInstance)

    /**
     * Close this scope and destroy all beans
     */
    public fun close()
}