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

package io.heartpattern.spikot.disposablecommand

import io.heartpattern.spikot.Spikot
import io.heartpattern.spikot.bean.Component
import io.heartpattern.spikot.bean.LoadOrder
import io.heartpattern.spikot.bean.inject
import io.heartpattern.spikot.extension.catchAll
import mu.KotlinLogging
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

public class DisposableCommand private constructor(
    public val uuid: UUID,
    public val owingPlayer: Player?,
    public val accessCount: Int?,
    public val expire: LocalDateTime,
    private val callback: (Player) -> Unit,
) {
    public val command: String = "${baseString}$uuid"
    public var remainAccessCount: Int? = accessCount
        private set

    public var isValid: Boolean = false
        private set

    private val removalJob = if (expire != LocalDateTime.MAX) object : BukkitRunnable() {
        override fun run() {
            dispose()
        }
    }.runTaskLater(plugin, Duration.between(LocalDateTime.now(), expire).toMillis() / 50)
    else null

    public fun dispose(): Boolean {
        if (!isValid) {
            return false
        }
        isValid = false
        commands.remove(uuid)
        removalJob?.cancel()

        return true
    }

    internal fun execute(player: Player) {
        remainAccessCount = remainAccessCount?.dec()
        if (remainAccessCount == 0)
            dispose()

        logger.catchAll("Exception thrown while executing command callback") {
            callback(player)
        }
    }

    @Component
    @LoadOrder(LoadOrder.FASTEST)
    public companion object {
        private val logger = KotlinLogging.logger {}
        private val plugin: Spikot by inject()

        private val baseString = "disposable-command-"
        private val expectedLength = ("/" + baseString + UUID.randomUUID()).length
        private val commands = HashMap<UUID, DisposableCommand>()

        @EventHandler
        private fun PlayerCommandPreprocessEvent.onCommandPreprocess() {
            if (message.length != expectedLength || !message.drop(1).startsWith(baseString))
                return

            isCancelled = true

            val uuid = UUID.fromString(message.substring(baseString.length + 1))
            val commandCallback = commands[uuid]
                ?: throw IllegalStateException("Command callback id $uuid is not found")

            commandCallback.execute(player)
        }

        @EventHandler
        private fun PlayerQuitEvent.onQuit() {
            commands.entries.removeIf { (_, value) ->
                value.owingPlayer == player
            }
        }

        public fun create(
            owingPlayer: Player? = null,
            accessCount: Int? = null,
            expire: LocalDateTime = LocalDateTime.MAX,
            callback: (Player) -> Unit
        ): DisposableCommand {
            val cmd = DisposableCommand(
                UUID.randomUUID(),
                owingPlayer,
                accessCount,
                expire,
                callback
            )
            commands[cmd.uuid] = cmd

            return cmd
        }
    }
}