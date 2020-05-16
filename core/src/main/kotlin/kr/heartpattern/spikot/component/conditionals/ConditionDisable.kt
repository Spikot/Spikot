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

package kr.heartpattern.spikot.component.conditionals

import kr.heartpattern.spikot.component.bean.BeanDefinition
import kr.heartpattern.spikot.component.conditional.Condition
import kr.heartpattern.spikot.component.conditional.ConditionContext
import kr.heartpattern.spikot.component.conditional.Conditional

/**
 * Disable bean
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Conditional(DisableCondition::class)
annotation class Disable

private object DisableCondition : Condition {
    override fun check(bean: BeanDefinition, context: ConditionContext): Boolean {
        return false
    }
}