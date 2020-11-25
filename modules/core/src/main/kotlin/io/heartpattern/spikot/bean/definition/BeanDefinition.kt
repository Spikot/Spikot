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

import io.heartpattern.spikot.annotation.MergedAnnotations
import io.heartpattern.spikot.bean.BeanDescription
import io.heartpattern.spikot.bean.BeanRegistry
import io.heartpattern.spikot.condition.ConditionContext
import kotlin.reflect.KClass

/**
 * Describe definition of bean
 */
public interface BeanDefinition {
    /**
     * Type of bean
     */
    public val type: KClass<*>

    /**
     * All merged annotation describe this bean
     */
    public val annotations: MergedAnnotations

    /**
     * Scope of this bean
     */
    public val scope: String

    /**
     * Name of this bean
     */
    public val name: String

    /**
     * Load order of this bean
     */
    public val loadOrder: Int

    /**
     * Whether this bean has primary priority in injection
     */
    public val isPrimary: Boolean

    /**
     * Whether this bean should load lazily. See [io.heartpattern.spikot.bean.Lazy]
     */
    public val isLazy: Boolean

    /**
     * Set of bean this bean depends on
     */
    public val dependsOn: Set<BeanDescription>

    /**
     * Create new instance of this bean.
     */
    public fun create(beanRegistry: BeanRegistry): Any

    /**
     * Check this bean satisfy condition
     */
    public fun checkCondition(conditionContext: ConditionContext): Boolean

    /**
     * Invoke initialize method of [bean]
     */
    public fun invokeInitializeFunction(bean: Any)

    /**
     * Invoke destroy method of [bean]
     */
    public fun invokeDestroyFunction(bean: Any)

    public fun injectProperty(bean: Any, beanRegistry: BeanRegistry)

    public val description: BeanDescription
        get() = BeanDescription.fromTypeAndName(type, name)
}