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

package kr.heartpattern.spikot.component.scope

import kr.heartpattern.spikot.component.Component
import kr.heartpattern.spikot.component.bean.*
import kr.heartpattern.spikot.component.conditional.ConditionContext
import kr.heartpattern.spikot.reflection.getObjectInstanceOrCreate

/**
 * Handle scope instance
 */
open class ScopeInstance<T : Component>(
    beans: BeanDefinitionSet
) {
    val beans: List<BeanInstance<T>>

    init {
        this.beans = beans.asSequence()
            .filter { it.checkCondition(ConditionContext(it.owingPlugin)) }
            .sortedBy { it.priority }
            .map { instantiate(it) }
            .toList()
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