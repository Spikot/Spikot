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

package io.heartpattern.spikot.component.scope

import io.heartpattern.spikot.component.Component
import io.heartpattern.spikot.component.bean.*
import io.heartpattern.spikot.component.conditional.ConditionContext
import io.heartpattern.spikot.extension.catchAll
import io.heartpattern.spikot.reflection.getObjectInstanceOrCreate
import mu.KotlinLogging
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

private val logger = KotlinLogging.logger {}

/**
 * Handle scope instance
 */
open class ScopeInstance<T : Component>(
    beans: BeanDefinitionSet
) : BeanInstanceRegistry<T> {
    private val _beans: Map<String, BeanInstance<T>> by lazy {
        beans.asSequence()
            .filter { it.checkCondition(ConditionContext(it.owingPlugin)) }
            .sortedBy { it.priority }
            .mapNotNull {
                logger.catchAll("Exception thrown while instantiate bean") {
                    instantiate(it)
                }
            }
            .map { it.instance::class.jvmName to it }
            .toMap()
    }

    override val beans: List<BeanInstance<T>>
        get() = _beans.values.toList()

    @Suppress("UNCHECKED_CAST")
    override fun <R : T> getBeanOfType(type: KClass<R>): BeanInstance<R>? {
        return _beans[type.jvmName] as BeanInstance<R>?
    }

    @Suppress("UNCHECKED_CAST")
    override fun <R : T> getBeansOfSubType(type: KClass<R>): List<BeanInstance<R>> {
        return beans.filter { type.isInstance(it.instance) } as List<BeanInstance<R>>
    }

    /**
     * Create instance of [bean]
     */
    protected open fun instantiate(bean: BeanDefinition): BeanInstance<T> {
        @Suppress("UNCHECKED_CAST")
        return BeanInstance(
            bean.type.getObjectInstanceOrCreate() as T,
            bean.owingPlugin
        )
    }

    /**
     * Call enable for all beans
     */
    fun enable() {
        for (bean in beans) {
            bean.enable()
        }
    }

    /**
     * Call disable for all beans
     */
    fun disable() {
        for (bean in beans.asReversed()) {
            bean.disable()
        }
    }
}