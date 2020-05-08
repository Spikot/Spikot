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

package kr.heartpattern.spikot.component.scopes.server

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.component.Component
import kr.heartpattern.spikot.component.scope.ScopeInstance

class ServerScopeInstance(
    plugin: SpikotPlugin
) : ScopeInstance<Component>(
    plugin.beanRegistry.getBeans(ServerScopeDefinition)
) {
    fun load() {
        for (bean in beans) {
            if (bean is ServerBeanInstance) {
                bean.load()
            }
        }
    }
}