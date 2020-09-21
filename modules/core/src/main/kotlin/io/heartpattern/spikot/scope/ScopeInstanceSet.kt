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
import org.bukkit.Bukkit

public class ScopeInstanceSet(
    public val scope: String,
    public val name: String,
    public val contextualObject: Map<String, Any> = emptyMap()
) {
    public var isInitialized: Boolean = false
        private set
    public var isDestroyed: Boolean = false
        private set

    private val scopes = LinkedHashMap<String, ScopeInstance>()

    public fun initialize() {
        check(!isInitialized) { "Cannot initialize scope instance set that already initialized" }
        check(!isDestroyed) { "Cannot initialize scope instacne set that is destroyed" }

        for (plugin in Bukkit.getPluginManager().plugins) { // Plugins are already sorted by dependency
            if (plugin !is SpikotPlugin)
                continue

            val scope = beanDefinitionRegistryScope(
                scope,
                name,
                plugin,
                plugin
            ) {
                for (depend in plugin.dependingSpikotPlugin) {
                    parents.add(scopes[depend.name]!!)
                }

                parents.add(plugin.singletonScope)

                contextualObject.putAll(this@ScopeInstanceSet.contextualObject)
            }

            scope.preInitializeBeanProcessor()
            scope.preInitializeBeans()

            scopes[plugin.name] = scope
        }

        isInitialized = true
    }

    public fun getScopeInstance(plugin: SpikotPlugin): ScopeInstance? {
        return scopes[plugin.name]
    }

    public fun destroy() {
        check(isInitialized) { "Cannot destroy scope instance set that is not initialized" }
        check(!isDestroyed) { "Cannot destroy scope instance set that already destroyed" }

        for (scope in scopes.values.reversed()) {
            scope.close()
        }

        isDestroyed = true
    }
}