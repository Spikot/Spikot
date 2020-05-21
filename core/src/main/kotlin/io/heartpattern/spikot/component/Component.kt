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

package io.heartpattern.spikot.component

import io.heartpattern.spikot.SpikotPlugin
import org.bukkit.event.Listener

/**
 * Component is a minimal fragment of plugin functionality. Component is same meaning with beans in spikot.
 */
abstract class Component : Listener {
    /**
     * Owing plugin of component
     */
    lateinit var plugin: SpikotPlugin
        internal set

    /**
     * Invoked when bean enabled and injection is done
     */
    open fun onEnable() {}

    /**
     * Invoked when bean is disabled
     */
    open fun onDisable() {}
}