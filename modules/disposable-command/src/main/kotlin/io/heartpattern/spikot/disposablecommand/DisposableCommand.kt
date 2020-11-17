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

import io.heartpattern.spikot.extension.catchAll
import kotlinx.coroutines.Job
import mu.KotlinLogging
import org.bukkit.entity.Player
import java.time.LocalTime
import java.util.*

private val logger = KotlinLogging.logger {}

public class DisposableCommand internal constructor(
    public val uuid: UUID,
    public val accessCount: Int?,
    public val expire: LocalTime,
    private val callback: (Player) -> Unit,
    private val removalTask: Job?
) {
    public val command: String = "${DisposableCommandManager.baseString}$uuid"
    public var remainAccessCount: Int? = accessCount
        private set

    public var isValid: Boolean = false
        private set

    public fun dispose(): Boolean {
        if (!isValid) {
            return false
        }
        isValid = false
        DisposableCommandManager.commandCallback.remove(uuid)
        removalTask?.cancel()

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
}