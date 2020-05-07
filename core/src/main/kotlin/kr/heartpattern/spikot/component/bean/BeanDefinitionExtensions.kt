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

package kr.heartpattern.spikot.component.bean

import kr.heartpattern.spikot.UniversalSingleton
import kr.heartpattern.spikot.component.Priority
import kr.heartpattern.spikot.component.conditional.ConditionContext
import kr.heartpattern.spikot.component.conditional.Conditional
import kr.heartpattern.spikot.component.scope.Scope
import kr.heartpattern.spikot.component.scope.ScopeDefinition
import kr.heartpattern.spikot.reflection.annotation.findMetaAnnotations

/**
 * Get priority
 */
val BeanDefinition.priority: Int
    get() = getMetaAnnotations<Priority>().firstOrNull()?.annotation?.priority ?: 0

/**
 * Get scope
 */
val BeanDefinition.scope: ScopeDefinition?
    get() = getMetaAnnotations<Scope>().firstOrNull()?.annotation?.scope?.objectInstance

/**
 * Check conditional to determine whether [this] should be loaded
 */
fun BeanDefinition.checkCondition(context: ConditionContext): Boolean {
    for (annotation in type.findMetaAnnotations<Conditional>())
        if (!UniversalSingleton[annotation.annotation.condition].check(type, context))
            return false

    return true
}