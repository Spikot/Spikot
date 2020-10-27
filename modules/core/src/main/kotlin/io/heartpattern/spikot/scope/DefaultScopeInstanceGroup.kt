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

/**
 * Scope instance that handle single plugin.
 */
public class DefaultScopeInstanceGroup<Q>(
    public override val plugin: SpikotPlugin,
    public override val scope: String,
    private val configurator: ScopeInstanceBuilder.(Q) -> Unit,
    private val scopeNameProvider: (Q) -> String = { "${plugin.name}-$scope-$it" }
) : ScopeInstanceGroup<Q> {
    private val scopes = HashMap<Q, ScopeInstance>()

    override val activeQualifiers: Collection<Q>
        get() = scopes.keys

    override operator fun get(qualifier: Q): ScopeInstance? {
        return scopes[qualifier]
    }

    internal fun create(qualifier: Q) {
        if (qualifier in activeQualifiers)
            throw IllegalArgumentException("Qualifier $qualifier is already registered")
        val scope = beanDefinitionRegistryScope(
            scope,
            scopeNameProvider(qualifier),
            plugin,
            plugin
        ) {
            configurator(qualifier)
        }

        scope.preInitializeBeanProcessor()
        scope.preInitializeBeans()

        scopes[qualifier] = scope
    }

    internal fun destroy(qualifier: Q) {
        val scope = scopes.remove(qualifier)
            ?: throw IllegalArgumentException("Qualifier $qualifier is not registered")

        scope.close()
    }
}