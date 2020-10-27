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

package io.heartpattern.spikot.packet

import com.google.common.collect.MultimapBuilder
import io.heartpattern.spikot.bean.AfterInitialize
import io.heartpattern.spikot.bean.BeforeDestroy
import io.heartpattern.spikot.bean.Component
import io.heartpattern.spikot.extension.catchAll
import io.heartpattern.spikot.util.forEachMergedException
import io.heartpattern.spikot.util.onlinePlayers
import mu.KotlinLogging
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

@Component
internal class PacketEventHandler(
    private val rawPacketCaptureInjector: RawPacketCaptureInjector
) {
    private val logger = KotlinLogging.logger {}

    @AfterInitialize
    private fun initialize() {
        for (player in onlinePlayers) {
            inject(player)
        }
    }

    @EventHandler
    private fun PlayerJoinEvent.onPlayerJoin() {
        inject(player)
    }

    @BeforeDestroy
    private fun destroy() {
        for (player in onlinePlayers) {
            rawPacketCaptureInjector.remove(player, "PacketEventHandler")
        }
    }

    private fun inject(player: Player) {
        rawPacketCaptureInjector.inject(player, "PacketEventHandler", object : RawPacketCapture {
            override fun capturePacket(packet: Any): Any? {
                val event = PacketEvent(player, packet)

                for (priority in priorityList) {
                    listeners.get(priority).forEach {
                        logger.catchAll("Exception thrown while handle PacketEventListener") {
                            it.handler(event)
                        }
                    }
                }

                return if (event.isCancelled)
                    null
                else
                    event.packet
            }
        })
    }

    companion object {
        private val priorityList = listOf(
            EventPriority.LOWEST,
            EventPriority.LOW,
            EventPriority.NORMAL,
            EventPriority.HIGH,
            EventPriority.HIGHEST,
            EventPriority.MONITOR
        )

        @Suppress("UnstableApiUsage")
        private val listeners = MultimapBuilder
            .enumKeys(EventPriority::class.java)
            .linkedListValues()
            .build<EventPriority, PacketEventListener>()

        fun register(listener: Any, priority: EventPriority, handler: (PacketEvent<Any>) -> Unit) {
            listeners.put(priority, PacketEventListener(
                listener,
                handler
            ))
        }

        fun unregister(listener: Any) {
            listeners.values().removeIf { it.listener === listener }
        }
    }
}

private data class PacketEventListener(val listener: Any, val handler: (PacketEvent<Any>) -> Unit)