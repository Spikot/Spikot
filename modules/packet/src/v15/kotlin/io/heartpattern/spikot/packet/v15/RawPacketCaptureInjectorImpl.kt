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

package io.heartpattern.spikot.packet.v15

import io.heartpattern.spikot.adapter.Adapter
import io.heartpattern.spikot.bean.Component
import io.heartpattern.spikot.bean.LoadOrder
import io.heartpattern.spikot.conditions.VersionCondition
import io.heartpattern.spikot.packet.RawPacketCapture
import io.heartpattern.spikot.packet.RawPacketCaptureInjector
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import mu.KotlinLogging
import net.minecraft.server.v1_15_R1.Packet
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer
import org.bukkit.entity.Player

@Adapter("1.15~1.15.2")
internal class RawPacketCaptureInjectorImpl : RawPacketCaptureInjector {
    private val logger = KotlinLogging.logger {}

    override fun inject(player: Player, name: String, capture: RawPacketCapture) {
        player.pipeline.addBefore(
            "packet_handler",
            name,
            object : ChannelDuplexHandler() {
                override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                    if (msg !is Packet<*>) {
                        logger.warn { "Unknown packet $msg" }
                        super.channelRead(ctx, msg)
                        return
                    }
                    val result = capture.capturePacket(msg)

                    if (result != null)
                        super.channelRead(ctx, result)
                }

                override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
                    if (msg !is Packet<*>) {
                        logger.warn { "Unknown packet $msg" }
                        super.write(ctx, msg, promise)
                        return
                    }
                    val result = capture.capturePacket(msg)

                    if (result != null)
                        super.write(ctx, result, promise)
                }
            }
        )
    }

    override fun remove(player: Player, name: String) {
        player.pipeline.remove(name)
    }

    private val Player.pipeline
        get() = (player as CraftPlayer).handle.playerConnection.networkManager.channel.pipeline()
}