/*
 * Copyright (c) 2020 HeartPattern and Spikot authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.heartpattern.spikot.player

import io.heartpattern.spikot.bean.Component
import io.heartpattern.spikot.bean.LoadOrder
import io.heartpattern.spikot.scope.ScopeInstanceSet
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import kotlin.collections.HashMap

@Component
@LoadOrder(LoadOrder.SYSTEM_LATEST)
internal class PlayerScopeHandler {
    private val scopeMap = HashMap<UUID, ScopeInstanceSet>()

    @EventHandler
    fun PlayerJoinEvent.onPlayerJoin() {
        val scopeSet = ScopeInstanceSet(
            "player",
            player.name,
            mapOf(
                "player" to player
            )
        )

        scopeSet.initialize()
        scopeMap[player.uniqueId] = scopeSet
    }

    @EventHandler
    fun PlayerQuitEvent.onPlayerQuit() {
        val removed = scopeMap.remove(player.uniqueId)
            ?: error("ScopeInstanceSet does not exist for ${player.name}")

        removed.destroy()
    }
}