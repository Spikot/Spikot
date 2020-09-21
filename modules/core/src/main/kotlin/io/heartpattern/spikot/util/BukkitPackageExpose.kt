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
import org.bukkit.OfflinePlayer
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.ServicesManager
import org.bukkit.plugin.messaging.Messenger
import org.bukkit.scheduler.BukkitScheduler
import java.util.*

// Expose Bukkit.* functions to package level scope

public val server: Server
    get() = Bukkit.getServer()

public val whitelistedPlayers: Collection<OfflinePlayer>
    get() = Bukkit.getWhitelistedPlayers()

public fun broadcastMessage(message: String): Int = Bukkit.broadcastMessage(message)
public fun broadcast(message: String, permission: String): Int = Bukkit.broadcast(message, permission)

public fun playerOf(name: String): Player? = Bukkit.getPlayer(name)
public fun exactPlayerOf(name: String): Player? = Bukkit.getPlayerExact(name)
public fun playerOf(uuid: UUID): Player? = Bukkit.getPlayer(uuid)
public fun matchPlayer(name: String): List<Player> = Bukkit.matchPlayer(name)
public fun offlinePlayerOf(id: UUID): OfflinePlayer = Bukkit.getOfflinePlayer(id)
public val onlinePlayers: Collection<Player>
    get() = Bukkit.getOnlinePlayers()

public val pluginManager: PluginManager
    get() = Bukkit.getPluginManager()

public val scheduler: BukkitScheduler
    get() = Bukkit.getScheduler()

public val servicesManager: ServicesManager
    get() = Bukkit.getServicesManager()

public val worlds: List<World>
    get() = Bukkit.getWorlds()

public fun worldOf(name: String): World? = Bukkit.getWorld(name)
public fun worldOf(uid: UUID): World? = Bukkit.getWorld(uid)

public fun pluginCommandOf(name: String): PluginCommand? = Bukkit.getPluginCommand(name)

public val messenger: Messenger
    get() = Bukkit.getMessenger()

