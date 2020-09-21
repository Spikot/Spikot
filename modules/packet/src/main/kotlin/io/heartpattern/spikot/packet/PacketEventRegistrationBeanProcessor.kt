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

package io.heartpattern.spikot.packet

import io.heartpattern.spikot.annotation.mergedAnnotations
import io.heartpattern.spikot.bean.BeanProcessor
import io.heartpattern.spikot.bean.BeanProcessorContext
import io.heartpattern.spikot.bean.Component
import io.heartpattern.spikot.bean.definition.BeanDefinition
import mu.KotlinLogging
import kotlin.reflect.KClass
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.isAccessible

private val logger = KotlinLogging.logger {}

@Component
private class PacketEventRegistrationBeanProcessor : BeanProcessor {
    override fun beforeInitialize(context: BeanProcessorContext, definition: BeanDefinition, bean: Any) {
        for (function in bean::class.functions) {
            val packetHandlerAnnotation = function.mergedAnnotations.get<PacketHandler>()
                ?.synthesize()
                ?: continue

            val parameters = function.parameters
            if (parameters.size != 2) {
                logger.error { "Cannot register packet event handler with ${parameters.size} parameters: $function" }
                continue
            }

            val eventType = parameters[1].type.classifier as KClass<*>
            if (eventType != PacketEvent::class) {
                logger.error { "Cannot register packet event handler with argument which is not PacketEvent" }
                continue
            }
            val packetType = parameters[1].type.arguments[0].type!!.classifier as KClass<*>

            function.isAccessible = true
            PacketEventHandler.register(bean, packetHandlerAnnotation.priority) {
                if (packetType.isInstance(it.packet))
                    function.call(bean, it)
            }
        }
    }

    override fun afterDestroy(context: BeanProcessorContext, definition: BeanDefinition, bean: Any) {
        PacketEventHandler.unregister(bean)
    }
}