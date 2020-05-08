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

package kr.heartpattern.spikot.test

import kr.heartpattern.spikot.component.scopes.inventory.InventoryBeanAccessor
import kr.heartpattern.spikot.component.scopes.inventory.InventoryComponent
import kr.heartpattern.spikot.component.scopes.inventory.InventoryScope

@InventoryScope
class TestInventoryComponent : InventoryComponent() {
    override fun onEnable() {
        println("Inventory open: ${player.name}")
        println(inventory)
    }

    override fun onDisable() {
        println("Inventory close: ${player.name}")
    }

    override fun toString(): String {
        return "Inventory1"
    }
}

@InventoryScope
class TestInventory2Component : InventoryComponent() {
    override fun onEnable() {
        println("Inventory open2: ${player.name}")
        println(inventory)

        println("Another module: ${TestInventoryAccessor[player]}")
    }

    override fun onDisable() {
        println("Inventory close2: ${player.name}")
    }

    override fun toString(): String {
        return "Inventory2"
    }
}

object TestInventoryAccessor : InventoryBeanAccessor<TestInventoryComponent>(TestInventoryComponent::class)