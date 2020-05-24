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

package io.heartpattern.spikot.command.failure

import io.heartpattern.spikot.command.CommandContext
import io.heartpattern.spikot.component.Component
import io.heartpattern.spikot.component.Priority
import io.heartpattern.spikot.component.bean.BeanInstance
import io.heartpattern.spikot.component.interceptor.BeanInterceptorComponent
import io.heartpattern.spikot.component.interceptor.Interceptor
import io.heartpattern.spikot.reflection.annotation.findMetaAnnotation
import mu.KotlinLogging
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmName

private val logger = KotlinLogging.logger {}

@Interceptor
internal object FailureEventRegistry : BeanInterceptorComponent() {
    private val registry = HashMap<KClass<*>, PriorityQueue<FailureHandlerHolder>>()

    override fun postEnable(bean: BeanInstance<Component>) {
        val instance = bean.instance

        for (method in instance::class.members) {
            method.findMetaAnnotation<SubscribeCommandFailure>() ?: continue

            val priority = method.findMetaAnnotation<Priority>()?.annotation?.priority ?: 0

            if (method.valueParameters.size != 2) {
                logger.error {
                    "Method annotated with @SubscribeCommandFailure should have one parameter: " +
                        "${instance::class.jvmName}.${method.name}"
                }
                continue
            }

            if (method.valueParameters[0].type.classifier != CommandContext::class) {
                logger.error {
                    "Method annotated with @SubscribeCommandFailure should have CommandContext as first parameter: " +
                        "${instance::class.jvmName}.${method.name}"
                }
                continue
            }

            val type = method.valueParameters[1].type.classifier as KClass<*>

            registry.getOrPut(type) {
                PriorityQueue(1) { a, b ->
                    a.priority - b.priority
                }
            }.add(FailureHandlerHolder(instance, type, priority) { node, failure ->
                method.call(instance, this@FailureHandlerHolder, node, failure)
            })
        }
    }

    override fun postDisable(bean: BeanInstance<Component>) {
        val instance = bean.instance

        val iter = registry.iterator()
        while (iter.hasNext()) {
            val (_, queue) = iter.next()
            val queueIter = queue.iterator()

            while (queueIter.hasNext())
                if (queueIter.next().owner === instance)
                    queueIter.remove()

            if (queue.isEmpty())
                iter.remove()
        }
    }

    fun <T : Any> getHandlerFor(type: KClass<T>): FailureHandler<T>? {
        return registry[type]?.peek()?.handler
    }

    private data class FailureHandlerHolder(
        val owner: Component,
        val type: KClass<*>,
        val priority: Int,
        val handler: FailureHandler<Any>
    )
}