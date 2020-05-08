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

package kr.heartpattern.spikot.component.scopes.player

import kr.heartpattern.spikot.component.bean.BeanDefinition
import kr.heartpattern.spikot.component.bean.BeanDefinitionSet
import kr.heartpattern.spikot.component.bean.BeanInstance
import kr.heartpattern.spikot.component.scope.ScopeInstance
import kr.heartpattern.spikot.reflection.createUnsafeInstance
import mu.KotlinLogging
import org.bukkit.entity.Player
import kotlin.reflect.full.isSubclassOf

private val logger = KotlinLogging.logger {}

class PlayerScopeInstance(
    beans: BeanDefinitionSet,
    private val player: Player
) : ScopeInstance<PlayerComponent>(
    beans
) {
    override fun instantiate(bean: BeanDefinition): BeanInstance<PlayerComponent> {
        if (!bean.type.isSubclassOf(PlayerComponent::class)) {
            throw IllegalArgumentException("Bean in PlayerScope should be subtype of PlayerComponent")
        }

        return PlayerBeanInstance(
            bean.type.createUnsafeInstance() as PlayerComponent,
            bean.owingPlugin,
            player
        )
    }
}