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

package kr.heartpattern.spikot.component.interceptor

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.component.classpath.scanMetaAnnotated
import kr.heartpattern.spikot.reflection.getObjectInstanceOrCreate
import mu.KotlinLogging
import java.util.*
import kotlin.reflect.jvm.jvmName

private val logger = KotlinLogging.logger {}

/**
 * Set of [BeanInterceptor]
 */
interface InterceptorRegistry {
    /**
     * Get all registered [BeanInterceptor]
     */
    fun getInterceptors(): Set<BeanInterceptor>

    /**
     * Add new [BeanInterceptor]
     */
    fun addInterceptor(interceptor: BeanInterceptor)

    /**
     * Remove [BeanInterceptor]
     */
    fun removeInterceptor(interceptor: BeanInterceptor)
}

/**
 * Basic implementation of [InterceptorRegistry]
 */
open class SimpleInterceptorRegistry : InterceptorRegistry {
    private val registry = mutableSetOf<BeanInterceptor>()

    override fun getInterceptors(): Set<BeanInterceptor> {
        return registry
    }

    override fun addInterceptor(interceptor: BeanInterceptor) {
        registry.add(interceptor)
    }

    override fun removeInterceptor(interceptor: BeanInterceptor) {
        registry.remove(interceptor)
    }
}

/**
 * Set of [BeanInterceptor] registered by [plugin]
 */
class PluginInterceptorRegistry(
    private val plugin: SpikotPlugin
) : SimpleInterceptorRegistry() {
    init {
        for (interceptor in plugin.classpathScanner.scanMetaAnnotated<Interceptor>()) {
            val instance = interceptor.getObjectInstanceOrCreate() as? BeanInterceptor
            if (instance == null) {
                logger.warn { "${interceptor.jvmName} is annotated with @Interceptor but not a subtype of BeanInterceptor" }
            } else {
                addInterceptor(instance)
            }
        }
    }

    override fun toString(): String {
        return "PluginInterceptorRegistry(${plugin.name})"
    }
}

/**
 * Set of all registered interceptor across all plugin.
 */
object UniversalInterceptorRegistry : InterceptorRegistry {
    private val registries = LinkedList<InterceptorRegistry>()

    private var cached: Set<BeanInterceptor>? = null

    override fun getInterceptors(): Set<BeanInterceptor> {
        if (cached == null) {
            cached = registries.flatMap { it.getInterceptors() }.toSet()
        }
        return cached!!
    }

    override fun addInterceptor(interceptor: BeanInterceptor) {
        throw UnsupportedOperationException("Add interceptor directly to UniversalInterceptorRegistry is not allowed")
    }

    override fun removeInterceptor(interceptor: BeanInterceptor) {
        throw UnsupportedOperationException("Remove interceptor directly to UniversalInterceptorRegistry is not allowed")
    }

    /**
     * Add new registry
     */
    fun addRegistry(registry: InterceptorRegistry) {
        if (registry === this) {
            throw IllegalArgumentException("Cannot register UniversalInterceptorRegistry to itself")
        }

        for (registered in registries) {
            if (registered === this) {
                logger.warn("Attempt to add registered registry: $registry")
                return
            }
        }

        registries.add(registry)
    }

    /**
     * Remove registry
     */
    fun removeRegistry(registry: InterceptorRegistry) {
        val iterator = registries.iterator()
        while (iterator.hasNext()) {
            if (iterator.next() === registry) {
                iterator.remove()
                return
            }
        }

        logger.warn("Attempt to remove unregistered registry: $registry")
    }
}