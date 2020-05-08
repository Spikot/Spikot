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

import kr.heartpattern.spikot.component.Component
import kr.heartpattern.spikot.component.bean.UniversalBeanRegistry
import kr.heartpattern.spikot.component.scopes.server.ServerScope
import mu.KotlinLogging
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

private val logger = KotlinLogging.logger {}

@ServerScope
internal object PlayerScopeHandler : Component() {
    internal val scopes = HashMap<UUID, PlayerScopeInstance>()

    @EventHandler
    fun PlayerJoinEvent.onJoin() {
        val scope = PlayerScopeInstance(
            UniversalBeanRegistry.getBeans(PlayerScopeDefinition),
            player
        )

        scopes[player.uniqueId] = scope
        scope.enable()
    }

    @EventHandler
    fun PlayerQuitEvent.onQuit() {
        val scope = scopes.remove(player.uniqueId)
        if (scope == null) {
            logger.warn("PlayerScope for ${player.name} is missing")
            return
        }

        scope.disable()
    }
}