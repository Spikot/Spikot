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

import io.heartpattern.spikot.bean.EarlyLoad
import io.heartpattern.spikot.bean.definition.BeanDefinition
import io.heartpattern.spikot.bean.definition.BeanDefinitionRegistry
import io.heartpattern.spikot.bean.definition.ClasspathBeanDefinitionRegistry
import io.heartpattern.spikot.classpath.Classpath
import io.heartpattern.spikot.classpath.JavaPluginClasspath
import io.heartpattern.spikot.extension.catchAll
import io.heartpattern.spikot.scope.BeanDefinitionRegistryScopeInstance
import io.heartpattern.spikot.scope.beanDefinitionRegistryScope
import io.heartpattern.spikot.type.TypedMap
import io.heartpattern.spikot.util.pluginOf
import kotlinx.coroutines.*
import mu.KotlinLogging
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

private val spikotLogger = KotlinLogging.logger {}

/**
 * Main class of plugin which use Spikot.
 * Plugin developer should extends this class instead of [JavaPlugin]
 */
public abstract class SpikotPlugin : JavaPlugin(), BeanDefinitionRegistry, Classpath, CoroutineScope {
    /**
     * Cache storage which can be generally used
     */
    public val cache: TypedMap = TypedMap()

    public val dependingPlugin: List<Plugin> = (description.softDepend + description.depend).mapNotNull(::pluginOf)

    public val dependingSpikotPlugin: List<SpikotPlugin> = dependingPlugin.filterIsInstance<SpikotPlugin>()

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main + CoroutinePlugin(this)

    private val classpath = JavaPluginClasspath.fromPlugin(this)
    private val beanDefinitionRegistry = ClasspathBeanDefinitionRegistry(classpath)
    public val singletonScope: BeanDefinitionRegistryScopeInstance = beanDefinitionRegistryScope(
        "singleton",
        name,
        this,
        this
    ) {
        dependingSpikotPlugin
            .map(SpikotPlugin::singletonScope)
            .forEach(parents::add)

        contextualObject["plugin"] = this@SpikotPlugin
        contextualObject[name] = this@SpikotPlugin
    }

    // Classpath delegate

    override val parents: Collection<Classpath>
        get() = classpath.parents

    override fun loadClass(name: String): KClass<*>? = classpath.loadClass(name)

    override fun getAllTypes(): Collection<String> = classpath.getAllTypes()

    override fun getTypesAnnotatedWith(annotation: KClass<out Annotation>): Collection<KClass<*>> = classpath.getTypesAnnotatedWith(annotation)

    override fun getFunctionsAnnotatedWith(annotation: KClass<out Annotation>): Collection<KFunction<*>> = classpath.getFunctionsAnnotatedWith(annotation)

    override fun <T : Any> getSubTypesOf(superType: KClass<T>): Collection<KClass<out T>> = classpath.getSubTypesOf(superType)

    //BeanDefinitionRegistry delegate

    override fun containsBeanDefinition(name: String): Boolean = beanDefinitionRegistry.containsBeanDefinition(name)

    override fun containsBeanDefinition(name: String, scope: String): Boolean = beanDefinitionRegistry.containsBeanDefinition(name, scope)

    override fun getBeanDefinition(name: String): BeanDefinition = beanDefinitionRegistry.getBeanDefinition(name)

    override fun getBeanDefinition(name: String, scope: String): BeanDefinition = beanDefinitionRegistry.getBeanDefinition(name, scope)

    override fun getAllBeanDefinition(): Collection<BeanDefinition> = beanDefinitionRegistry.getAllBeanDefinition()

    override fun getAllBeanDefinition(scope: String): Collection<BeanDefinition> = beanDefinitionRegistry.getAllBeanDefinition(scope)

    // Overriding

    final override fun onLoad() {
        spikotLogger.trace { "Load $this" }
        singletonScope.preInitializeBeanProcessor()

        beanDefinitionRegistry.getAllBeanDefinition("singleton").asSequence()
            .sortedBy { it.loadOrder }
            .filter { it.annotations.has<EarlyLoad>() }
            .forEach { definition ->
                spikotLogger.catchAll("Exception thrown while initializing early load bean $definition") {
                    if (singletonScope.hasBean(definition.description)) // Filter conditionally disabled bean
                        singletonScope.getBean(definition.description)
                }
            }
    }

    final override fun onEnable() {
        spikotLogger.trace { "Enable $this" }
        beanDefinitionRegistry.getAllBeanDefinition("singleton").asSequence()
            .sortedBy { it.loadOrder }
            .filter { !it.annotations.has<EarlyLoad>() }
            .forEach { definition ->
                spikotLogger.catchAll("Exception thrown while initializing early load bean $definition") {
                    if (singletonScope.hasBean(definition.description))
                        singletonScope.getBean(definition.description)
                }
            }
    }

    final override fun onDisable() {
        spikotLogger.trace { "Disable $this" }
        singletonScope.close()
        cancel(CancellationException("Plugin shutdown"))
    }

    public companion object {
        public val allSpikotPlugins: List<SpikotPlugin>
            get() = Bukkit.getPluginManager().plugins.filterIsInstance<SpikotPlugin>()
    }
}