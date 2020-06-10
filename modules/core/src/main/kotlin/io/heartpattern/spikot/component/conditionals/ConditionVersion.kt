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

package io.heartpattern.spikot.component.conditionals

import io.heartpattern.spikot.component.bean.BeanDefinition
import io.heartpattern.spikot.component.conditional.Condition
import io.heartpattern.spikot.component.conditional.ConditionContext
import io.heartpattern.spikot.component.conditional.Conditional
import io.heartpattern.spikot.util.MINECRAFT_VERSION

/**
 * Load bean only if minecraft version of server is [version]
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Conditional(ConditionVersion::class)
annotation class VersionCondition(val version: String)

private object ConditionVersion : Condition {
    override fun check(bean: BeanDefinition, context: ConditionContext): Boolean {
        val targetVersion = bean.getAttribute(VersionCondition::class, "version") as String
        return targetVersion == MINECRAFT_VERSION
    }
}