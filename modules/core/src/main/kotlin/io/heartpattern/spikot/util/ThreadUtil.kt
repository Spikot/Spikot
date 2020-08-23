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

package io.heartpattern.spikot.util

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

/**
 * Run [block] in main thread
 */
public fun Plugin.runSync(block: () -> Unit) {
    if (Bukkit.isPrimaryThread()) {
        block()
    } else {
        Bukkit.getScheduler().runTask(this, block)
    }
}

/**
 * Run [block] in async thread
 */
public fun Plugin.runAsync(block: () -> Unit): BukkitTask {
    return Bukkit.getScheduler().runTaskAsynchronously(this, block)
}

/**
 * Run [block] at the last of current tick
 */
public fun Plugin.runLastSync(block: () -> Unit) {
    Bukkit.getScheduler().runTask(this, block)
}

/**
 * Run [block] in main thread after [ticks] tick
 */
public fun Plugin.runSyncDelayed(ticks: Long, block: () -> Unit): BukkitTask {
    return Bukkit.getScheduler().runTaskLater(this, block, ticks)
}

/**
 * Run [block] in async thread after [ticks] tick
 */
public fun Plugin.runAsyncDelayed(ticks: Long, block: () -> Unit): BukkitTask {
    return Bukkit.getScheduler().runTaskLaterAsynchronously(this, block, ticks)
}