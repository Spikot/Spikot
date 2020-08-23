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

package io.heartpattern.spikot.scope

import io.heartpattern.spikot.SpikotPlugin
import io.heartpattern.spikot.annotation.AliasFor
import io.heartpattern.spikot.bean.*
import io.heartpattern.spikot.bean.definition.DefaultBeanDefinitionRegistry
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

interface Service

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ServiceType(@get:AliasFor(Qualifier::class) val name: String)

@Component
@ServiceType("impl1")
class ServiceImpl1 : Service

@Component
@ServiceType("impl2")
class ServiceImpl2 : Service

@Component
class ServiceImpl3 : Service {
    @ServiceType("impl1")
    val impl1 by inject<Service>()

    val impl2 by inject<ServiceImpl2>()
}

@Component
class ServiceImpl4(
    val impl1: ServiceImpl1
) : Service {
    @Qualifier("impl2")
    val impl2 by inject<Service>()
    val impl3 by inject<ServiceImpl3>()
}

class BeanDefinitionRegistryScopeInstanceInjectionTest {
    @Test
    fun injectTest() {
        val registry = DefaultBeanDefinitionRegistry(
            setOf(
                ServiceImpl1::class.toDefinition(),
                ServiceImpl2::class.toDefinition(),
                ServiceImpl3::class.toDefinition(),
                ServiceImpl4::class.toDefinition()
            )
        )

        val scope = BeanDefinitionRegistryScopeInstance(
            "singleton",
            "test",
            mockk<SpikotPlugin>(),
            registry,
            emptySet(),
            mapOf()
        )

        scope.preInitializeBeans()

        val impl1 = scope.getBean<ServiceImpl1>()
        val impl2 = scope.getBean<ServiceImpl2>()
        val impl3 = scope.getBean<ServiceImpl3>()
        val impl4 = scope.getBean<ServiceImpl4>()

        assertTrue(impl3.impl1 === impl1)
        assertTrue(impl3.impl2 === impl2)
        assertTrue(impl4.impl1 === impl1)
        assertTrue(impl4.impl2 === impl2)
        assertTrue(impl4.impl3 === impl3)
    }
}