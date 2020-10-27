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

package io.heartpattern.spikot

import io.heartpattern.spikot.scope.ScopeHandler
import io.heartpattern.spikot.scope.ScopeInstance
import io.heartpattern.spikot.scope.ScopeInstanceGroup

public object SingletonScopeHandler : ScopeHandler<Unit> {
    override fun get(plugin: SpikotPlugin): ScopeInstanceGroup<Unit> {
        return object: ScopeInstanceGroup<Unit>{
            override val plugin: SpikotPlugin
                get() = plugin
            override val scope: String
                get() = "singleton"
            override val activeQualifiers: Collection<Unit>
                get() = listOf(Unit)

            override fun get(qualifier: Unit): ScopeInstance {
                return plugin.singletonScope
            }
        }
    }
}