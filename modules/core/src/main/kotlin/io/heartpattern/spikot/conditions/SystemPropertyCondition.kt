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

@Condition(SystemPropertyConditionEvaluator::class)
@Retention(AnnotationRetention.RUNTIME)
public annotation class SystemPropertyCondition(
    val property: String,
    val value: String = ""
)

@Condition(SystemPropertyConditionEvaluator::class)
public annotation class SystemPropertyAbsentCondition(
    val property: String
)

private class SystemPropertyConditionEvaluator : ConditionEvaluator {
    override fun check(bean: BeanDefinition, context: ConditionContext): Boolean {
        val exist = bean.annotations.getAll<SystemPropertyCondition>()
            .map { it.synthesize() }

        val absent = bean.annotations.getAll<SystemPropertyAbsentCondition>()
            .map { it.getTypedAttribute<String>("property") }

        for (property in exist)
            if (System.getProperty(property.property) ?: System.getenv(property.property) != property.value)
                return false


        for(property in absent)
            if((System.getProperty(property) ?: System.getenv(property)) != null)
                return false

        return true
    }
}