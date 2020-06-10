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

package io.heartpattern.spikot.component.bean

import io.heartpattern.spikot.SpikotPlugin
import io.heartpattern.spikot.component.Component
import io.heartpattern.spikot.component.interceptor.*
import io.heartpattern.spikot.extension.catchAll
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * Class wrapping bean [instance] owned by [plugin]
 */
open class BeanInstance<out T : Component>(
    val instance: T,
    val plugin: SpikotPlugin
) {
    init {
        instance.plugin = plugin
    }

    /**
     * Enable bean and call interceptor
     */
    fun enable() {
        UniversalInterceptorRegistry.preEnable(this)
        logger.catchAll("Exception thrown while enabling component $instance") {
            instance.onEnable()
        }
        UniversalInterceptorRegistry.postEnable(this)
    }

    /**
     * Disable bean and call interceptor
     */
    fun disable() {
        UniversalInterceptorRegistry.preDisable(this)
        logger.catchAll("Exception thrown while disabling component $instance") {
            instance.onDisable()
        }
        UniversalInterceptorRegistry.postDisable(this)
    }
}