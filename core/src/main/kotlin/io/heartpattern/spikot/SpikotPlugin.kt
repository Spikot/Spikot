/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.heartpattern.spikot

import io.heartpattern.spikot.component.Component
import io.heartpattern.spikot.component.bean.BeanDefinitionRegistry
import io.heartpattern.spikot.component.bean.BeanInstanceRegistry
import io.heartpattern.spikot.component.bean.PluginBeanDefinitionRegistry
import io.heartpattern.spikot.component.bean.UniversalBeanDefinitionRegistry
import io.heartpattern.spikot.component.classpath.AllClassScanner
import io.heartpattern.spikot.component.classpath.ClasspathScanner
import io.heartpattern.spikot.component.classpath.ReflectionsClasspathScanner
import io.heartpattern.spikot.component.classpath.UniversalClasspathScanner
import io.heartpattern.spikot.component.interceptor.InterceptorRegistry
import io.heartpattern.spikot.component.interceptor.PluginInterceptorRegistry
import io.heartpattern.spikot.component.interceptor.UniversalInterceptorRegistry
import io.heartpattern.spikot.component.scopes.server.ServerScopeInstance
import io.heartpattern.spikot.coroutine.CoroutinePlugin
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ConfigurationBuilder
import kotlin.coroutines.CoroutineContext

/**
 * Main class of plugin which use Spikot.
 * Plugin developer should extends this class instead of [JavaPlugin]
 */
@Suppress("LeakingThis")
abstract class SpikotPlugin : JavaPlugin(), CoroutineScope {
    val classpathScanner: ClasspathScanner
    val beanDefinitionRegistry: BeanDefinitionRegistry
    val interceptorRegistry: InterceptorRegistry
    private lateinit var serverScopeInstance: ServerScopeInstance
    val serverScopeBeanInstanceRegistry: BeanInstanceRegistry<Component>
        get() = serverScopeInstance

    init {
        logger.info("Start classpath scanning for $name")
        classpathScanner = ReflectionsClasspathScanner(
            ConfigurationBuilder()
                .addUrls(file.toURI().toURL())
                .addScanners(
                    TypeAnnotationsScanner(),
                    AllClassScanner()
                )
                .addClassLoaders(
                    classLoader,
                    Bukkit::class.java.classLoader
                )
                .run {
                    Reflections(this)
                }
        )
        UniversalClasspathScanner.addScanner(classpathScanner)
        logger.info("Total ${classpathScanner.getAllTypes().size} class found")

        beanDefinitionRegistry = PluginBeanDefinitionRegistry(this)
        UniversalBeanDefinitionRegistry.addRegistry(beanDefinitionRegistry)

        interceptorRegistry = PluginInterceptorRegistry(this)
        UniversalInterceptorRegistry.addRegistry(interceptorRegistry)
    }

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main + CoroutinePlugin(this)

    final override fun onLoad() {
        serverScopeInstance = ServerScopeInstance(this)
        serverScopeInstance.load()
    }

    final override fun onEnable() {
        serverScopeInstance.enable()
    }

    final override fun onDisable() {
        serverScopeInstance.disable()
        UniversalInterceptorRegistry.removeRegistry(interceptorRegistry)
        UniversalBeanDefinitionRegistry.removeRegistry(beanDefinitionRegistry)
        UniversalClasspathScanner.removeScanner(classpathScanner)
        cancel(CancellationException("Plugin shutdown"))
    }

    companion object {
        val allSpikotPlugins: List<SpikotPlugin>
            get() = Bukkit.getPluginManager().plugins.filterIsInstance<SpikotPlugin>()
    }
}