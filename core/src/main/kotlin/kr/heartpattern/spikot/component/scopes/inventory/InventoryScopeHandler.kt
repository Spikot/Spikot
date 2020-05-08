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

package kr.heartpattern.spikot.component.scopes.inventory

import kr.heartpattern.spikot.component.Component
import kr.heartpattern.spikot.component.bean.UniversalBeanRegistry
import kr.heartpattern.spikot.component.scopes.server.ServerScope
import mu.KotlinLogging
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import java.util.*
import kotlin.collections.HashMap

private val logger = KotlinLogging.logger {}

@ServerScope
internal object InventoryScopeHandler : Component() {
    internal val scopes = HashMap<UUID, InventoryScopeInstance>()

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun InventoryOpenEvent.onOpen() {
        if (scopes.containsKey(player.uniqueId))
            throw IllegalStateException("New inventory is opened while previous inventory does not closed")

        val scope = InventoryScopeInstance(
            UniversalBeanRegistry.getBeans(InventoryScopeDefinition),
            player as Player,
            inventory
        )

        scopes[player.uniqueId] = scope
        scope.enable()
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun InventoryCloseEvent.onClose() {
        val scope = scopes.remove(player.uniqueId)

        if (scope == null) {
            logger.warn("InventoryScope for ${player.name} is missing")
            return
        }

        scope.disable()
    }
}