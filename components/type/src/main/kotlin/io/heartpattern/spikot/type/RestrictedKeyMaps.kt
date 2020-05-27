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

package io.heartpattern.spikot.type

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

class PlayerMap<V>(
    private val default: (Player) -> V
) : RestrictedKeyMap<Player, V>() {
    override val keys: Set<Player>
        get() = Bukkit.getOnlinePlayers().toSet()

    override fun default(key: Player): V {
        return default.invoke(key)
    }

    operator fun get(key: UUID): V? {
        return this[Bukkit.getPlayer(key)]
    }
}

class PluginMap<V>(
    private val default: (Plugin) -> V
) : RestrictedKeyMap<Plugin, V>() {
    override val keys: Set<Plugin>
        get() = Bukkit.getPluginManager().plugins.toSet()

    override fun default(key: Plugin): V {
        return default.invoke(key)
    }
}