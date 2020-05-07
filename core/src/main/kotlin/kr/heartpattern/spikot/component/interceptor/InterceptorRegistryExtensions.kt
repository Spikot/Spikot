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

import kr.heartpattern.spikot.component.Component
import kr.heartpattern.spikot.util.catchAll
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * Iterate all registered interceptor
 */
inline fun InterceptorRegistry.forEachInterceptor(block: (BeanInterceptor) -> Unit) {
    for (interceptor in getInterceptors()) {
        block(interceptor)
    }
}

private inline fun InterceptorRegistry.forEachInterceptorCatching(state: String, block: (BeanInterceptor) -> Unit) {
    forEachInterceptor {
        logger.catchAll("Exception thrown while intercepting $state") {
            block(it)
        }
    }
}

/**
 * Iterate all registered interceptor and call preEnable
 */
fun InterceptorRegistry.preEnable(bean: Component) {
    forEachInterceptorCatching("pre enable") {
        it.preEnable(bean)
    }
}

/**
 * Iterate all registered interceptor and call postEnable
 */
fun InterceptorRegistry.postEnable(bean: Component) {
    forEachInterceptorCatching("post enable") {
        it.postEnable(bean)
    }
}

/**
 * Iterate all registered interceptor and call preDisable
 */
fun InterceptorRegistry.preDisable(bean: Component) {
    forEachInterceptorCatching("pre disable") {
        it.preDisable(bean)
    }
}

/**
 * Iterate all registered interceptor and call postDisable
 */
fun InterceptorRegistry.postDisable(bean: Component) {
    forEachInterceptorCatching("post disable") {
        it.postDisable(bean)
    }
}