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
import io.heartpattern.spikot.bean.*
import io.heartpattern.spikot.bean.definition.DefaultBeanDefinitionRegistry
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

interface TestService {
    val asserter: SequenceAsserter

    @AfterInitialize
    fun initialize() {
        asserter("${this::class.simpleName}-init")
    }

    @BeforeDestroy
    fun destroy() {
        asserter("${this::class.simpleName}-destroy")
    }
}

@Component
@LoadOrder(LoadOrder.FASTEST)
class ImplFastest : TestService {
    override val asserter: SequenceAsserter by inject()
}

@Component
@LoadOrder(LoadOrder.FAST)
class ImplFast : TestService {
    override val asserter: SequenceAsserter by inject()
}

@Component
@Primary
class ImplNormal : TestService {
    override val asserter: SequenceAsserter by inject()
}

@Component
@LoadOrder(LoadOrder.LATE)
class ImplLate : TestService {
    override val asserter: SequenceAsserter by inject()
}

@Component
@LoadOrder(LoadOrder.LATEST)
class ImplLatest : TestService {
    override val asserter: SequenceAsserter by inject()
}

@Component
@Lazy
class ImplLazy : TestService {
    override val asserter: SequenceAsserter by inject()
}

class BeanDefinitionRegistryScopeInstanceTest {
    @Test
    fun loadOrderTest() {
        val registry = DefaultBeanDefinitionRegistry(
            setOf(
                ImplFastest::class.toDefinition(),
                ImplFast::class.toDefinition(),
                ImplLate::class.toDefinition(),
                ImplLatest::class.toDefinition(),
                ImplNormal::class.toDefinition(),
                ImplLazy::class.toDefinition(),
            )
        )

        val asserter = SequenceAsserter()

        val scope = BeanDefinitionRegistryScopeInstance(
            "singleton",
            "test",
            mockk<SpikotPlugin>(),
            registry,
            emptySet(),
            mapOf(
                "sequenceAsserter" to asserter
            )
        )

        scope.preInitializeBeans()
        with(asserter) {
            check("ImplFastest-init")
            check("ImplFast-init")
            check("ImplNormal-init")
            check("ImplLate-init")
            check("ImplLatest-init")
        }

        assertTrue(scope.getBean(BeanDescription.fromType(ImplFastest::class)) is ImplFastest)
        assertTrue(scope.getBean(BeanDescription.fromType(ImplFast::class)) is ImplFast)
        assertTrue(scope.getBean(BeanDescription.fromType(ImplNormal::class)) is ImplNormal)
        assertTrue(scope.getBean(BeanDescription.fromType(ImplLate::class)) is ImplLate)
        assertTrue(scope.getBean(BeanDescription.fromType(ImplLatest::class)) is ImplLatest)

        asserter.checkEmpty()

        assertTrue(scope.getBean(BeanDescription.fromType(ImplLazy::class)) is ImplLazy)
        with(asserter) {
            check("ImplLazy-init")
        }

        scope.close()
        with(asserter) {
            check("ImplLazy-destroy")
            check("ImplLatest-destroy")
            check("ImplLate-destroy")
            check("ImplNormal-destroy")
            check("ImplFast-destroy")
            check("ImplFastest-destroy")
        }
    }
}