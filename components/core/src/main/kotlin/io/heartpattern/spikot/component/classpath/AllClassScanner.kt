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

package io.heartpattern.spikot.component.classpath

import org.reflections.Reflections
import org.reflections.Store
import org.reflections.scanners.AbstractScanner

/**
 * Get all class scanned by [this]
 */
fun Reflections.scanAllClass(): Collection<String> {
    return store.get(AllClassScanner::class.java, "all")
}

/**
 * Scan all class
 */
class AllClassScanner : AbstractScanner() {
    override fun scan(cls: Any, store: Store) {
        val name = metadataAdapter.getClassName(cls)
        put(store, "all", name)
    }
}