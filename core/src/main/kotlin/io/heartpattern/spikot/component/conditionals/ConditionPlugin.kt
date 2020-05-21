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
import org.bukkit.Bukkit

/**
 * Load bean only if all [plugins] is loaded
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Conditional(ConditionPlugin::class)
annotation class PluginCondition(vararg val plugins: String)

private object ConditionPlugin : Condition {
    override fun check(bean: BeanDefinition, context: ConditionContext): Boolean {
        val annotations = bean.getMetaAnnotations<PluginCondition>()
        val pluginManager = Bukkit.getPluginManager()

        @Suppress("UNCHECKED_CAST")
        for (annotation in annotations)
            for (plugin in annotation.getAttribute("plugins") as Array<String>)
                if (io.heartpattern.spikot.util.pluginOf(plugin) == null)
                    return false

        return true
    }
}