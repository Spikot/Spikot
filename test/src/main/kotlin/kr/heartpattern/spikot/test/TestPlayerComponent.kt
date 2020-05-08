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

import kr.heartpattern.spikot.component.Priority
import kr.heartpattern.spikot.component.scopes.player.PlayerComponent
import kr.heartpattern.spikot.component.scopes.player.PlayerScope
import kr.heartpattern.spikot.component.scopes.player.playerBean
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent

@PlayerScope
class TestPlayerComponent : PlayerComponent() {
    private val test2 by playerBean<TestPlayer2Component>()

    override fun onEnable() {
        println("Player join: ${player.name}")
        println(plugin.name)
        println(test2)
    }

    @EventHandler
    fun PlayerInteractEvent.onInteract() {
        println("Interact: ${player.name}")
    }

    override fun onDisable() {
        println("Player quit: ${player.name}")
    }

    override fun toString(): String {
        return "Player1"
    }
}

@PlayerScope
@Priority(-1)
class TestPlayer2Component : PlayerComponent() {
    override fun onEnable() {
        println("Player join2: ${player.name}")
        println(plugin.name)
    }

    @EventHandler
    fun PlayerInteractEvent.onInteract() {
        println("Interact2: ${player.name}")
    }

    override fun onDisable() {
        println("Player quit2: ${player.name}")
    }

    override fun toString(): String {
        return "Player2"
    }
}