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

package io.heartpattern.spikot.event

import io.heartpattern.spikot.SpikotPlugin
import org.bukkit.event.*
import org.bukkit.event.player.PlayerEvent
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.RegisteredListener
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Await until next event arrive filtered by [filter]
 */
public suspend inline fun <reified E : Event> SpikotPlugin.awaitNextEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline filter: (E) -> Boolean = { true }
): E {
    return suspendCoroutine<E> { cont ->
        val eventType = E::class.java
        val handlerListFunction = eventType.getDeclaredMethod("getHandlerList")
        val handlerList = handlerListFunction.invoke(null) as HandlerList
        val listener = object : Listener {}
        val registeredListener = RegisteredListener(
            listener,
            EventExecutor { _, event ->
                if (event !is E || (event is Cancellable && event.isCancelled && ignoreCancelled) || !filter(event))
                    return@EventExecutor

                handlerList.unregister(listener)
                cont.resume(event)
            },
            priority,
            this,
            ignoreCancelled
        )
        handlerList.register(registeredListener)
    }
}

/**
 * Await until next event filtered by [filter] and cancel
 */
public suspend inline fun <reified E> SpikotPlugin.awaitCancelNextEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline filter: (E) -> Boolean = { true }
): E where E : Event, E : Cancellable {
    return suspendCoroutine<E> { cont ->
        val eventType = E::class.java
        val handlerListFunction = eventType.getDeclaredMethod("getHandlerList")
        val handlerList = handlerListFunction.invoke(null) as HandlerList
        val listener = object : Listener {}
        val registeredListener = RegisteredListener(
            listener,
            EventExecutor { _, event ->
                if (event !is E || (event.isCancelled && ignoreCancelled) || !filter(event))
                    return@EventExecutor

                handlerList.unregister(listener)
                event.isCancelled = true
                cont.resume(event)
            },
            priority,
            this,
            ignoreCancelled
        )
        handlerList.register(registeredListener)
    }
}

public suspend inline fun <reified E : PlayerEvent> SpikotPlugin.awaitNextPlayerEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline filter: (E) -> Boolean = { true }
): E {
    return awaitNextEvent(
        priority,
        ignoreCancelled
    ) {
        filter(it)
    }
}

public suspend inline fun <reified E : PlayerEvent> SpikotPlugin.awaitCancelNextPlayerEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline filter: (E) -> Boolean = { true }
): E {
    return awaitCancelNextEvent(
        priority,
        ignoreCancelled
    ) {
        filter(it)
    }
}