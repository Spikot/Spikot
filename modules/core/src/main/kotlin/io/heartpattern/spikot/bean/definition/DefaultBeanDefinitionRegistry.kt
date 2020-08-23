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

public class DefaultBeanDefinitionRegistry(
    definitions: Collection<BeanDefinition>
) : BeanDefinitionRegistry {
    private val registry = definitions.asSequence()
        .map { it.name to it }
        .toMap()

    override fun containsBeanDefinition(name: String): Boolean {
        return name in registry
    }

    override fun containsBeanDefinition(name: String, scope: String): Boolean {
        val found = registry[name]
        return found?.scope == scope
    }

    override fun getBeanDefinition(name: String): BeanDefinition {
        return registry[name] ?: throw BeanDefinitionNotFoundException(name)
    }

    override fun getBeanDefinition(name: String, scope: String): BeanDefinition {
        val found = registry[name]
        if (found?.scope != scope)
            throw BeanDefinitionNotFoundException(name)

        return found
    }

    override fun getAllBeanDefinition(): Collection<BeanDefinition> {
        return registry.values
    }

    override fun getAllBeanDefinition(scope: String): Collection<BeanDefinition> {
        return registry.values.filter { it.scope == scope }
    }
}