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

package kr.heartpattern.spikot.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KVisibility
import kotlin.reflect.jvm.jvmName

/**
 * Get object instance of [this]. This function also try to find object instance of private class.
 * @throws [IllegalArgumentException] [this] is not an object instance
 */
fun <T : Any> KClass<T>.getUnsafeObjectInstance(): T {
    return getUnsafeObjectInstanceOrNull()
        ?: throw IllegalArgumentException("$jvmName is not object")
}

/**
 * Get object instance of [this] or null if fail to find object instance.
 * This function also try to find object instance of private class.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> KClass<T>.getUnsafeObjectInstanceOrNull(): T? {
    return if (this.visibility != KVisibility.PUBLIC) {
        try {
            val objectInstanceField = this.java.getDeclaredField("INSTANCE")
            objectInstanceField.withAccessibility {
                objectInstanceField.get(null) as T
            }
        } catch (e: NoSuchFieldException) {
            null
        }
    } else {
        this.objectInstance
    }
}

/**
 * Create new instance of [this]. This function can create instance with private constructor.
 * @throws [IllegalArgumentException] [this] does not has no-arg constructor.
 */
fun <T : Any> KClass<T>.createUnsafeInstance(): T {
    return createUnsafeInstanceOrNull()
        ?: throw IllegalArgumentException("$jvmName does not have no-arg constructor")
}

/**
 * Create new instance of [this] or null if failed to find no-arg constructor.
 * This function can create instance with private constructor.
 */
fun <T : Any> KClass<T>.createUnsafeInstanceOrNull(): T? {
    val constructor = constructors.find { it.parameters.all(KParameter::isOptional) }
        ?: return null

    return constructor.withAccessibility {
        constructor.call()
    }
}

/**
 * Create object instance of [this] if [this] is object, or create instance if [this] has no-arg constructor.
 * @throws [IllegalArgumentException] [this] is not object and does not have no-arg constructor
 */
fun <T : Any> KClass<T>.getObjectInstanceOrCreate(): T {
    return getUnsafeObjectInstanceOrNull()
        ?: createUnsafeInstanceOrNull()
        ?: throw IllegalArgumentException("Cannot find object instance or no-arg constructor of $jvmName")
}