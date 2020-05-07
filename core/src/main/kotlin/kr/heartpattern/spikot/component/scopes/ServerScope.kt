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

package kr.heartpattern.spikot.component.scopes

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.component.Component
import kr.heartpattern.spikot.component.interceptor.BeanInterceptor
import kr.heartpattern.spikot.component.interceptor.UniversalInterceptorRegistry
import kr.heartpattern.spikot.component.interceptor.forEachInterceptor
import kr.heartpattern.spikot.component.scope.Scope
import kr.heartpattern.spikot.component.scope.ScopeDefinition
import kr.heartpattern.spikot.component.scope.ScopeInstance
import kr.heartpattern.spikot.util.catchAll
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * Annotate server scoped bean
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Scope(ServerScopeDefinition::class)
annotation class ServerScope

/**
 * Server scope definition
 */
object ServerScopeDefinition : ScopeDefinition {
    override val name: String = "singleton"
}

/**
 * Component which can also handle load state of [org.bukkit.plugin.java.JavaPlugin]
 */
abstract class ServerComponent : Component() {
    /**
     * Called in plugin load state
     */
    open fun onLoad() {}
}

/**
 * Interceptor which can also intercept load state of [ServerComponent]
 */
interface ServerBeanInterceptor : BeanInterceptor {
    fun preLoad(bean: ServerComponent) {}
    fun postLoad(bean: ServerComponent) {}
}

internal class ServerScopeInstance(
    plugin: SpikotPlugin
) : ScopeInstance<Component>(
    plugin.beanRegistry.getBeans(ServerScopeDefinition)
) {
    fun load() {
        for (bean in beans) {
            val instance = bean.instance
            if (instance is ServerComponent) {
                UniversalInterceptorRegistry.forEachInterceptor { interceptor ->
                    if (interceptor is ServerBeanInterceptor) {
                        logger.catchAll("Exception thrown while intercepting pre load") {
                            interceptor.preLoad(instance)
                        }
                    }
                }

                logger.catchAll("Exception thrown while loading component $instance") {
                    instance.onLoad()
                }

                UniversalInterceptorRegistry.forEachInterceptor { interceptor ->
                    if (interceptor is ServerBeanInterceptor) {
                        logger.catchAll("Exception thrown while intercepting post load") {
                            interceptor.postLoad(instance)
                        }
                    }
                }
            }
        }
    }
}