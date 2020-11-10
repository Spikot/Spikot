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

package io.heartpattern.spikot.conditions

import io.heartpattern.spikot.bean.definition.BeanDefinition
import io.heartpattern.spikot.condition.Condition
import io.heartpattern.spikot.condition.ConditionContext
import io.heartpattern.spikot.condition.ConditionEvaluator
import io.heartpattern.spikot.util.MINECRAFT_VERSION
import io.heartpattern.spikot.util.Version

@Condition(VersionConditionEvaluator::class)
@Retention(AnnotationRetention.RUNTIME)
public annotation class VersionCondition(val version: String)

private object VersionConditionEvaluator : ConditionEvaluator {
    override fun check(bean: BeanDefinition, context: ConditionContext): Boolean {
        val version = bean.annotations.get<VersionCondition>()!!.getTypedAttribute<String>("version")

        return if (version.contains("~")) { // Range
            val token = version.split('~')
            val minimumRaw = token[0].trim()
            val maximumRaw = token[1].trim()
            val minimum = if (minimumRaw.isEmpty()) Version.MINIMUM else Version.fromString(minimumRaw)
            val maximum = if (maximumRaw.isEmpty()) Version.MAXIMUM else Version.fromString(maximumRaw)
            MINECRAFT_VERSION in minimum..maximum
        } else {
            MINECRAFT_VERSION == Version.fromString(version)
        }
    }
}