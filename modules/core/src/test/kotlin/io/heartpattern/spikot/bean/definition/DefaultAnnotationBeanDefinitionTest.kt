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

package io.heartpattern.spikot.bean.definition

import io.heartpattern.spikot.bean.*
import io.heartpattern.spikot.util.toFirstLowerCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.KClass

class DefaultAnnotationBeanDefinitionTest {
    @Test
    fun informationParseTest() {
        check(DefaultBean::class)
        check(TestQualifierBean::class, name = "Test")
        check(PrimaryBean::class, isPrimary = true)
        check(DummyDependencyBean::class, dependsOn = setOf(
            BeanDescription.fromName("dummyBeanA"),
            BeanDescription.fromName("dummyBeanB")
        ))
        check(DummyInjectBean::class, dependsOn = setOf(
            BeanDescription.fromType(DummyBeanA::class),
            BeanDescription.fromType(DummyBeanB::class)
        ))
        check(LateLoadOrderBean::class, loadOrder = LoadOrder.LATE)
        check(LazyBean::class, isLazy = true)
    }

    @Test
    fun creationTest() {
        val dummyA = DummyBeanA()
        val dummyB = DummyBeanB()
        val registry = InstanceBeanRegistry(mapOf(
            "dummyBeanA" to InstanceBeanRegistry.BeanHolder(dummyA),
            "dummyBeanB" to InstanceBeanRegistry.BeanHolder(dummyB)
        ))

        val missingRegistry = InstanceBeanRegistry(mapOf(
            "dummyBeanA" to InstanceBeanRegistry.BeanHolder(dummyA)
        ))

        checkCreation<DefaultBean>(registry)
        checkCreation<DummyDependencyBean>(registry)
        val injectBean = checkCreation<DummyInjectBean>(registry)
        assertEquals(injectBean.a, dummyA)
        assertEquals(injectBean.b, dummyB)
        assertEquals(injectBean.b2, dummyB)

        assertThrows<BeanNotFoundException> {
            checkCreation<DummyDependencyBean>(missingRegistry)
        }

        assertThrows<BeanNotFoundException> {
            checkCreation<DummyInjectBean>(missingRegistry)
        }
    }

    private inline fun <reified T : Any> checkCreation(registry: BeanRegistry): T {
        val defaultBean = T::class.toDefinition()
        val defaultBeanInstance = defaultBean.create(registry)
        assertTrue(defaultBeanInstance is T)
        return defaultBeanInstance as T
    }

    private fun check(
        type: KClass<*>,
        scope: String = "singleton",
        name: String = type.simpleName!!.toFirstLowerCase(),
        loadOrder: Int = 0,
        isPrimary: Boolean = false,
        isLazy: Boolean = false,
        dependsOn: Set<BeanDescription> = emptySet()
    ) {
        val definition = DefaultAnnotationBeanDefinition.fromClass(type)

        assertEquals(definition.type, type)
        assertEquals(definition.scope, scope)
        assertEquals(definition.name, name)
        assertEquals(definition.loadOrder, loadOrder)
        assertEquals(definition.isPrimary, isPrimary)
        assertEquals(definition.isLazy, isLazy)
        assertEquals(definition.dependsOn, dependsOn)
    }
}