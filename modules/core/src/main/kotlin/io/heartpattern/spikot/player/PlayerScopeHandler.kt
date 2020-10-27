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

import io.heartpattern.spikot.SingletonScopeHandler
import io.heartpattern.spikot.SpikotPlugin
import io.heartpattern.spikot.bean.AfterInitialize
import io.heartpattern.spikot.bean.BeforeDestroy
import io.heartpattern.spikot.bean.Component
import io.heartpattern.spikot.bean.LoadOrder
import io.heartpattern.spikot.scope.DefaultScopeHandler
import io.heartpattern.spikot.util.forEachMergedException
import io.heartpattern.spikot.util.onlinePlayers
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@Component
@LoadOrder(LoadOrder.SYSTEM_LATEST)
public object PlayerScopeHandler : DefaultScopeHandler<Player>() {
    override val scope: String = "player"

    override fun DefaultScopeHandlerConfig.configure(plugin: SpikotPlugin, qualifier: Player) {
        parent(SingletonScopeHandler) { Unit }
        contextualObject("player", qualifier)
    }

    @AfterInitialize
    private fun init(){
        println("Init ${onlinePlayers.size}")
        onlinePlayers.forEachMergedException("Exception thrown while enabling player scope", ::create)
    }

    @BeforeDestroy
    private fun die(){
        println("Die ${onlinePlayers.size}")
        onlinePlayers.forEachMergedException("Exception thrown while disabling player scope", ::destroy)
    }

    @EventHandler
    private fun PlayerJoinEvent.onJoin(){
        create(player)
    }

    @EventHandler
    private fun PlayerQuitEvent.onQuit(){
        destroy(player)
    }
}