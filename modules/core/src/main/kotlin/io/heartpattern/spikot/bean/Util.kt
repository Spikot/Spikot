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
@file:JvmName("BeanUtil")

package io.heartpattern.spikot.bean

import io.heartpattern.spikot.annotation.MergedAnnotations
import io.heartpattern.spikot.bean.definition.BeanDefinition
import io.heartpattern.spikot.scope.ScopeInstance
import io.heartpattern.spikot.util.forEachMergedException
import io.heartpattern.spikot.util.toFirstLowerCase
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

internal fun MergedAnnotations.resolveBeanName(): String? {
    return get<Qualifier>()
        ?.getTypedAttribute("name")
}

internal fun KClass<*>.resolveBeanName(): String {
    return simpleName?.toFirstLowerCase() ?: jvmName.toFirstLowerCase()
}

public fun BeanDefinition.initializeBean(instance: Any, scope: ScopeInstance) {
    injectProperty(instance, scope)
    val context = BeanProcessorContext(scope)
    scope.allBeanProcessors.forEachMergedException("Exception thrown while invoke bean processors") { name ->
        (scope.getBean(BeanDescription.fromName(name)) as BeanProcessor).beforeInitialize(
            context, this, instance
        )
    }
    invokeInitializeFunction(instance)
}

public fun BeanDefinition.createAndInitialize(scope: ScopeInstance): Any{
    val instance = create(scope)
    initializeBean(instance, scope)
    return instance
}

public fun BeanDefinition.destroyBean(instance: Any, scope: ScopeInstance){
    val context = BeanProcessorContext(scope)
    scope.allBeanProcessors.forEachMergedException("Exception thrown while invoke bean processors") { name ->
        (scope.getBean(BeanDescription.fromName(name)) as BeanProcessor).afterDestroy(
            context, this, instance
        )
    }
    invokeDestroyFunction(instance)
}