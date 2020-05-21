/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.heartpattern.spikot.util

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

/**
 * Run [block] in main thread
 */
fun Plugin.runSync(block: () -> Unit) {
    if (Bukkit.isPrimaryThread()) {
        block()
    } else {
        Bukkit.getScheduler().runTask(this, block)
    }
}

/**
 * Run [block] in async thread
 */
fun Plugin.runAsync(block: () -> Unit): BukkitTask {
    return Bukkit.getScheduler().runTaskAsynchronously(this, block)
}

/**
 * Run [block] at the last of current tick
 */
fun Plugin.runLastSync(block: () -> Unit) {
    Bukkit.getScheduler().runTask(this, block)
}

/**
 * Run [block] in main thread after [ticks] tick
 */
fun Plugin.runSyncAfter(ticks: Long, block: () -> Unit): BukkitTask {
    return Bukkit.getScheduler().runTaskLater(this, block, ticks)
}

/**
 * Run [block] in async thread after [ticks] tick
 */
fun Plugin.runAsyncAfter(ticks: Long, block: () -> Unit): BukkitTask {
    return Bukkit.getScheduler().runTaskLaterAsynchronously(this, block, ticks)
}