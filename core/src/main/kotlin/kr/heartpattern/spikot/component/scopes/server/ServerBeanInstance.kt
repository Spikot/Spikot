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

package kr.heartpattern.spikot.component.scopes.server

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.component.bean.BeanInstance
import kr.heartpattern.spikot.component.interceptor.UniversalInterceptorRegistry
import kr.heartpattern.spikot.component.interceptor.forEachInterceptor
import kr.heartpattern.spikot.util.catchAll
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class ServerBeanInstance(
    instance: ServerComponent,
    plugin: SpikotPlugin
) : BeanInstance<ServerComponent>(instance, plugin) {
    fun load() {
        UniversalInterceptorRegistry.forEachInterceptor { interceptor ->
            if (interceptor is ServerBeanInterceptor) {
                logger.catchAll("Exception thrown while intercepting pre load") {
                    interceptor.preLoad(this)
                }
            }
        }

        logger.catchAll("Exception thrown while loading component $instance") {
            instance.onLoad()
        }

        UniversalInterceptorRegistry.forEachInterceptor { interceptor ->
            if (interceptor is ServerBeanInterceptor) {
                logger.catchAll("Exception thrown while intercepting post load") {
                    interceptor.postLoad(this)
                }
            }
        }
    }
}