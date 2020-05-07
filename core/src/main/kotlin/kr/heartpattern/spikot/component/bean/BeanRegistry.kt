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

package kr.heartpattern.spikot.component.bean

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.component.classpath.scanMetaAnnotated
import kr.heartpattern.spikot.component.scope.Scope
import kr.heartpattern.spikot.component.scope.ScopeDefinition
import mu.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger {}

/**
 * Registry of beans
 */
interface BeanRegistry {
    /**
     * Get registered bean of [scope]
     */
    fun getBeans(scope: ScopeDefinition): BeanDefinitionSet

    /**
     * Register [bean] to [scope]
     */
    fun addBean(scope: ScopeDefinition, bean: BeanDefinition)
}

/**
 * Basic implementation of [BeanRegistry]
 */
open class SimpleBeanRegistry : BeanRegistry {
    private val registry: MutableMap<String, MutableSet<BeanDefinition>> = mutableMapOf()

    override fun getBeans(scope: ScopeDefinition): BeanDefinitionSet {
        return registry[scope.name] ?: emptySet()
    }

    override fun addBean(scope: ScopeDefinition, bean: BeanDefinition) {
        registry.getOrPut(scope.name) { mutableSetOf() }.add(bean)
    }
}

/**
 * Registry of beans in plugin
 */
class PluginBeanRegistry(
    val plugin: SpikotPlugin
) : SimpleBeanRegistry() {
    init {
        for (bean in plugin.classpathScanner.scanMetaAnnotated<Scope>()) {
            val definition = BeanDefinition(bean, plugin)
            val scope = definition.scope

            if (scope == null) {
                logger.warn { "Bean ${bean.qualifiedName} defined in unknown scope" }
                continue
            }

            addBean(scope, definition)
        }
    }

    override fun toString(): String {
        return "PluginBeanRegistry(${plugin.name})"
    }
}

/**
 * Registry of all registered beans across the plugin
 */
object UniversalBeanRegistry : BeanRegistry {
    private val registries = LinkedList<BeanRegistry>()

    /**
     * Add [registry] to [UniversalBeanRegistry]
     */
    fun addRegistry(registry: BeanRegistry) {
        if (registry === this) {
            throw IllegalArgumentException("Cannot add UniversalBeanRegistry")
        }

        for (registered in registries) {
            if (registered === registry) {
                logger.warn("Attempt to add registered registry: $registry")
                return
            }
        }

        registries.add(registry)
    }

    /**
     * Remove [registry] from [UniversalBeanRegistry]
     */
    fun removeRegistry(registry: BeanRegistry) {
        val iterator = registries.iterator()
        while (iterator.hasNext()) {
            val registered = iterator.next()
            if (registered === registry) {
                iterator.remove()
                return
            }
        }

        logger.warn("Attempt to remove unregistered registry: $registry")
    }

    override fun addBean(scope: ScopeDefinition, bean: BeanDefinition) {
        throw UnsupportedOperationException("Add bean directly to UniversalBeanRegistry is not allowed")
    }

    override fun getBeans(scope: ScopeDefinition): BeanDefinitionSet {
        return registries.asSequence()
            .flatMap { it.getBeans(scope).asSequence() }
            .toSet()
    }
}