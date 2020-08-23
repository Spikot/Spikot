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
import io.heartpattern.spikot.util.ignore
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private class TestBean {
    @Qualifier("stringBean")
    val str by inject<String>()

    val asserter by inject<SequenceAsserter>()

    private fun initialize() {
        asserter("test-init")
    }

    private fun destroy() {
        asserter("test-destroy")
    }
}

private class TestBean2(val testBean: TestBean) {
    val asserter by inject<SequenceAsserter>()

    @AfterInitialize
    private fun initialize() {
        asserter("test2-init")
    }

    @BeforeDestroy
    private fun destroy() {
        asserter("test2-destroy")
    }
}

@Component
private class ProviderComponent {
    @Bean
    fun stringBean(): String {
        return "hello"
    }

    @Qualifier("testName")
    @Bean(initialize = "initialize", destroy = "destroy")
    fun test(@Qualifier("stringBean") str: String): TestBean {
        str.ignore()
        return TestBean()
    }

    @Bean
    fun test2(@Qualifier("testName") test1: TestBean): TestBean2 {
        return TestBean2(test1)
    }

    @get:Bean
    val localPropertyBean: Long = 5L
}

@get:Bean
val topPropertyBean: Int = 3

@Bean
fun topFunctionBean(): String {
    return "top"
}

class BeanDefinitionRegistryScopeInstanceProviderTest {
    @Test
    fun providerTest() {
        val asserter = SequenceAsserter()

        val registry = DefaultBeanDefinitionRegistry(
            setOf(
                ProviderComponent::class.toDefinition(),
                ProviderComponent::stringBean.toDefinition(),
                ProviderComponent::test.toDefinition(),
                ProviderComponent::test2.toDefinition(),
                ::topPropertyBean.getter.toDefinition(),
                ::topFunctionBean.toDefinition(),
                ProviderComponent::localPropertyBean.getter.toDefinition(),
            )
        )

        val scope = BeanDefinitionRegistryScopeInstance(
            "singleton",
            "test",
            mockk<SpikotPlugin>(),
            registry,
            setOf(),
            mapOf(
                "asserter" to asserter
            )
        )

        val string = scope.getBean<String>("stringBean")
        val topString = scope.getBean<String>("topFunctionBean")
        val int = scope.getBean<Int>("topPropertyBean")
        val long = scope.getBean<Long>("localPropertyBean")
        val test1 = scope.getBean<Any>("testName") as TestBean
        val test2 = scope.getBean<TestBean2>()

        assertEquals("top", topString)
        assertEquals(3, int)
        assertEquals(5, long)
        assertTrue(test1 === test2.testBean)
        assertTrue(test1.str === string)
        assertEquals("hello", string)
        assertThrows<IllegalArgumentException> {
            scope.getBean<String>()
        }

        scope.close()

        with(asserter) {
            check("test-init")
            check("test2-init")
            check("test2-destroy")
            check("test-destroy")
            checkEmpty()
        }
    }
}