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

package kr.heartpattern.spikot.adapter

import kr.heartpattern.spikot.SpikotPlugin
import kotlin.reflect.KClass

inline fun <reified T : Any> getAdapterOf(): T {
    return getAdapterOf(T::class)
}

fun <T : Any> getAdapterOf(type: KClass<T>): T {
    @Suppress("UNCHECKED_CAST")
    return SpikotPlugin.allSpikotPlugins
        .asSequence()
        .flatMap { it.serverScopeBeanInstanceRegistry.beans.asSequence() }
        .firstOrNull { type.isInstance(it) }
        ?.instance as? T
        ?: throw AdapterImplementationNotFoundException(type)
}