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
import io.heartpattern.spikot.bean.AfterInitialize
import io.heartpattern.spikot.bean.BeforeDestroy
import io.heartpattern.spikot.util.forEachMergedException
import mu.KotlinLogging
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent

private val logger = KotlinLogging.logger {}

public abstract class DefaultScopeHandler<Q> : ScopeHandler<Q> {
    private val instances = LinkedHashMap<String, DefaultPluginGroupScopeInstance<Q>>()
    private val scopeInfo = LinkedHashMap<Q, ScopeInfo>()

    public val activeQualifiers: Set<Q>
        get() = scopeInfo.keys

    public abstract val scope: String
    protected abstract fun DefaultScopeHandlerConfig.configure(plugin: SpikotPlugin, qualifier: Q)

    override operator fun get(plugin: SpikotPlugin): ScopeInstanceGroup<Q>? = instances[plugin.name]

    @AfterInitialize
    internal fun initialize() {
        Bukkit.getPluginManager().plugins
            .filterIsInstance<SpikotPlugin>()
            .filter{it.isEnabled}
            .forEachMergedException("Exception thrown while initialize $scope", ::addPlugin)
    }

    @BeforeDestroy
    internal fun destroy() {
        Bukkit.getPluginManager().plugins.reversed()
            .filterIsInstance<SpikotPlugin>()
            .filter{it.isEnabled}
            .forEachMergedException("Exception thrown while destroy $scope", ::removePlugin)
    }

    @EventHandler
    private fun PluginEnableEvent.onEnable() {
        val spikotPlugin = plugin as? SpikotPlugin ?: return
        addPlugin(spikotPlugin)
    }

    @EventHandler
    private fun PluginDisableEvent.onDisable() {
        val spikotPlugin = plugin as? SpikotPlugin ?: return
        removePlugin(spikotPlugin)
    }

    private fun addPlugin(plugin: SpikotPlugin) {
        if (instances.contains(plugin.name))
            throw IllegalArgumentException("$plugin is already registered")

        logger.trace { "Create new ScopeInstanceGroup for $plugin" }

        val instance = DefaultPluginGroupScopeInstance<Q>(
            plugin,
            scope,
            { qualifier ->
                val config = DefaultScopeHandlerConfig(this, plugin)
                config.configure(plugin, qualifier)
                plugin.deepDependingSpikotPlugin.forEachMergedException("Exception thrown while adding parent scope") { depend ->
                    val group = this@DefaultScopeHandler[depend]
                        ?: throw IllegalStateException("Depending plugin $depend is not loaded")

                    val scope = group[qualifier]
                        ?: throw IllegalStateException("Depending scope ${this@DefaultScopeHandler} with $qualifier is not loaded in $plugin")

                    parents.add(scope)
                }
            }
        )

        instances[plugin.name] = instance

        scopeInfo.entries.forEachMergedException("Exception thrown while enabling $scope for $plugin") { (qualifier, info) ->
            if (info.pluginSelector(plugin))
                instance.create(qualifier)
        }
    }

    private fun removePlugin(plugin: SpikotPlugin) {
        val instance = instances[plugin.name]
            ?: throw IllegalArgumentException("$plugin is not registered")

        logger.trace { "Destroy ScopeInstanceGroup for $plugin" }

        scopeInfo.entries.reversed().forEachMergedException("Exception thrown while disabling $scope for $plugin") { (qualifier, _) ->
            if (qualifier in instance.activeQualifiers)
                instance.destroy(qualifier)
        }
    }

    protected fun create(qualifier: Q, pluginSelector: (SpikotPlugin) -> Boolean = { true }) {
        if (qualifier in scopeInfo)
            throw IllegalArgumentException("Qualifier $qualifier already exist in $scope")

        scopeInfo[qualifier] = ScopeInfo(pluginSelector)

        instances.values.forEachMergedException("Exception thrown while creating $qualifier for $scope") {
            if(pluginSelector(it.plugin))
                it.create(qualifier)
        }
    }

    protected fun destroy(qualifier: Q) {
        scopeInfo.remove(qualifier) ?: throw IllegalArgumentException("Qualifier $qualifier is not registered in $scope")

        instances.values.reversed().forEachMergedException("Exception thrown while destroying $qualifier for $scope") {
            if(qualifier in it.activeQualifiers)
                it.destroy(qualifier)
        }
    }

    public inner class DefaultScopeHandlerConfig internal constructor(
        private val builder: ScopeInstanceBuilder,
        private val plugin: SpikotPlugin
    ) {
        public fun <P> parents(handler: ScopeHandler<P>, link: (SpikotPlugin) -> P) {
            (plugin.dependingSpikotPlugin + plugin).forEachMergedException("Exception thrown while adding parent scope") inner@{ depend ->
                val qualifier = link(depend)
                val group = handler[depend]
                    ?: throw IllegalStateException("Depending plugin $depend is not loaded")
                val scope = group[qualifier]
                    ?: throw IllegalStateException("Depending scope $handler with $qualifier is not loaded in $plugin")
                builder.parents.add(scope)
            }
        }

        public fun <P> parentsIfExist(handler: ScopeHandler<P>, link: (SpikotPlugin) -> P) {
            (plugin.dependingSpikotPlugin + plugin).forEachMergedException("Exception thrown while adding parent scope") inner@{ depend ->
                val qualifier = link(depend)
                val group = handler[depend]
                    ?: return@inner
                val scope = group[qualifier]
                    ?: return@inner
                builder.parents.add(scope)
            }
        }

        public fun parent(instance: ScopeInstance) {
            builder.parents.add(instance)
        }

        public fun contextualObject(name: String, value: Any) {
            builder.contextualObject[name] = value
        }
    }

    private data class ScopeInfo(
        val pluginSelector: (SpikotPlugin) -> Boolean
    )
}