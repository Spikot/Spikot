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

package kr.heartpattern.spikot

import kr.heartpattern.spikot.reflection.getObjectInstanceOrCreate
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

/**
 * Manage singleton instance across all plugin
 */
object UniversalSingleton {
    private val instances = HashMap<String, Any>()

    /**
     * Get singleton instance of [type]
     */
    @Suppress("UNCHECKED_CAST")
    @Synchronized
    operator fun <T : Any> get(type: KClass<T>): T {
        return instances.getOrPut(type.jvmName) {
            type.getObjectInstanceOrCreate()
        } as T
    }
}