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

import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import io.heartpattern.spikot.bean.Bean
import io.heartpattern.spikot.bean.Component
import io.heartpattern.spikot.classpath.Classpath
import io.heartpattern.spikot.classpath.getFunctionsMetaAnnotatedWith
import io.heartpattern.spikot.classpath.getTypesMetaAnnotatedWith
import io.heartpattern.spikot.reflection.isAnnotation

public class ClasspathBeanDefinitionRegistry(
    classpath: Classpath
) : BeanDefinitionRegistry {
    private val nameRegistry: Map<String, BeanDefinition>
    private val scopeRegistry: Multimap<String, BeanDefinition>

    init {
        val nameRegistry = HashMap<String, BeanDefinition>(256)

        @Suppress("UnstableApiUsage")
        val scopeRegistry = MultimapBuilder.hashKeys().linkedListValues().build<String, BeanDefinition>()

        for (type in classpath.getTypesMetaAnnotatedWith<Component>()) {
            if (type.isAnnotation)
                continue

            val definition = DefaultAnnotationBeanDefinition.fromClass(type)
            nameRegistry[definition.name] = definition
            scopeRegistry.put(definition.scope, definition)
        }

        for (function in classpath.getFunctionsMetaAnnotatedWith<Bean>()) {
            val definition = DefaultAnnotationBeanDefinition.fromFunction(function)
            nameRegistry[definition.name] = definition
            scopeRegistry.put(definition.scope, definition)
        }

        this.nameRegistry = nameRegistry
        this.scopeRegistry = scopeRegistry
    }

    override fun containsBeanDefinition(name: String): Boolean {
        return nameRegistry.containsKey(name)
    }

    override fun containsBeanDefinition(name: String, scope: String): Boolean {
        return nameRegistry[name]?.scope == scope
    }

    override fun getBeanDefinition(name: String): BeanDefinition {
        return nameRegistry[name] ?: throw BeanDefinitionNotFoundException(name)
    }

    override fun getBeanDefinition(name: String, scope: String): BeanDefinition {
        val found = nameRegistry[name]
        if (found?.scope != scope)
            throw BeanDefinitionNotFoundException(name)

        return found
    }

    override fun getAllBeanDefinition(): Collection<BeanDefinition> {
        return nameRegistry.values
    }

    override fun getAllBeanDefinition(scope: String): Collection<BeanDefinition> {
        return scopeRegistry.get(scope)
    }
}