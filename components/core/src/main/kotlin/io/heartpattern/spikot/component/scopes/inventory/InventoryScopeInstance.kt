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

package io.heartpattern.spikot.component.scopes.inventory

import io.heartpattern.spikot.component.bean.BeanDefinition
import io.heartpattern.spikot.component.bean.BeanDefinitionSet
import io.heartpattern.spikot.component.bean.BeanInstance
import io.heartpattern.spikot.component.scope.ScopeInstance
import io.heartpattern.spikot.reflection.createUnsafeInstance
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import kotlin.reflect.full.isSubclassOf

class InventoryScopeInstance(
    beans: BeanDefinitionSet,
    private val player: Player,
    private val inventory: Inventory
) : ScopeInstance<InventoryComponent>(
    beans
) {
    override fun instantiate(bean: BeanDefinition): BeanInstance<InventoryComponent> {
        if (!bean.type.isSubclassOf(InventoryComponent::class)) {
            throw IllegalArgumentException("Bean in InventoryScope should be subtype of InventoryComponent")
        }

        return InventoryBeanInstance(
            bean.type.createUnsafeInstance() as InventoryComponent,
            bean.owingPlugin,
            player,
            inventory
        )
    }
}