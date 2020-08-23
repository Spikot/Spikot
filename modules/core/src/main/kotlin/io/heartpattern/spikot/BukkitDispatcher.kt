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

package io.heartpattern.spikot

import com.google.auto.service.AutoService
import kotlinx.coroutines.*
import kotlinx.coroutines.internal.MainDispatcherFactory
import org.bukkit.Bukkit.getScheduler
import org.bukkit.Bukkit.isPrimaryThread
import org.bukkit.plugin.Plugin
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal data class CoroutinePlugin(val plugin: Plugin) : AbstractCoroutineContextElement(CoroutinePlugin) {
    companion object Key : CoroutineContext.Key<CoroutinePlugin>

    override fun toString(): String {
        return "CoroutinePlugin(${plugin.name})"
    }
}

@Suppress("unused")
@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
public val Dispatchers.Bukkit: MainCoroutineDispatcher
    get() = BukkitDispatcher

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
internal sealed class DispatcherBukkit : MainCoroutineDispatcher(), Delay {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        getScheduler().runTask(
            context[CoroutinePlugin]?.plugin
                ?: throw IllegalStateException("CoroutineContext have no CoroutinePlugin"),
            block
        )
    }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val task = getScheduler().runTaskLater(
            continuation.context[CoroutinePlugin]?.plugin
                ?: throw IllegalStateException("CoroutineContext have no CoroutinePlugin"), java.lang.Runnable {
            with(continuation) {
                resumeUndispatched(Unit)
            }
        }, timeMillis / 50)
        continuation.invokeOnCancellation { task.cancel() }
    }
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AutoService(MainDispatcherFactory::class)
internal class BukkitDispatcherFactory : MainDispatcherFactory {
    override val loadPriority: Int
        get() = 0

    override fun createDispatcher(allFactories: List<MainDispatcherFactory>): MainCoroutineDispatcher = BukkitDispatcher
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
private object ImmediateBukkitDispatcher : DispatcherBukkit() {
    override val immediate: MainCoroutineDispatcher
        get() = this

    override fun isDispatchNeeded(context: CoroutineContext): Boolean = !isPrimaryThread()

    override fun toString() = "Bukkit [immediate]"
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
internal object BukkitDispatcher : DispatcherBukkit() {
    override val immediate: MainCoroutineDispatcher
        get() = ImmediateBukkitDispatcher

    override fun toString() = "Bukkit"
}

public suspend fun delayTick(tick: Long) {
    delay(tick * 50)
}