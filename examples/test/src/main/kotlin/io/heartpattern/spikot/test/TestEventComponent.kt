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

package io.heartpattern.spikot.test

import io.heartpattern.spikot.bean.AfterInitialize
import io.heartpattern.spikot.bean.BeforeDestroy
import io.heartpattern.spikot.bean.Component
import io.heartpattern.spikot.bean.inject
import io.heartpattern.spikot.scope.BeanDefinitionRegistryScopeInstance
import mu.KotlinLogging
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@Component
class TestEventComponent {
    private val logger = KotlinLogging.logger {}

    private val injected: TestComponent by inject()
    private val scope by inject<BeanDefinitionRegistryScopeInstance>()

    @AfterInitialize
    fun initialize() {
        logger.info("Enable test event component")
    }

    @BeforeDestroy
    fun destroy() {
        logger.info("Disable test event component")
    }

    @EventHandler
    private fun PlayerJoinEvent.onPlayerJoin() {
        Bukkit.broadcastMessage("Hello, ${player.name}")
    }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        Bukkit.broadcastMessage("Bye, ${event.player.name}")
    }
}