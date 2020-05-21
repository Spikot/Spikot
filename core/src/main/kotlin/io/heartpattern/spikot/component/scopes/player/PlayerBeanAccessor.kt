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

package io.heartpattern.spikot.component.scopes.player

import org.bukkit.entity.Player
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmName

/**
 * Provide access to [PlayerComponent]
 */
abstract class PlayerBeanAccessor<T : PlayerComponent>(private val type: KClass<T>) {
    operator fun get(player: Player): T {
        return getPlayerBean(player, type)
    }
}

/**
 * Get player bean of type [T] for player of [this] component
 */
inline fun <reified T : PlayerComponent> PlayerComponent.playerBean(): Lazy<T> {
    return playerBean(T::class)
}

/**
 * Get player bean of [type] for player of [this] component
 */
fun <T : PlayerComponent> PlayerComponent.playerBean(type: KClass<T>): Lazy<T> {
    return lazy {
        getPlayerBean(player, type)
    }
}

/**
 * Property delegate to get player bean typed [T]
 */
inline fun <reified T : PlayerComponent> playerBeanDelegate(): ReadOnlyProperty<Player, T> {
    return playerBeanDelegate(T::class)
}

/**
 * Property delegate to get player bean typed [type]
 */
fun <T : PlayerComponent> playerBeanDelegate(type: KClass<T>): ReadOnlyProperty<Player, T> {
    return object : ReadOnlyProperty<Player, T> {
        override fun getValue(thisRef: Player, property: KProperty<*>): T {
            return getPlayerBean(thisRef, type)
        }
    }
}

private fun <T : PlayerComponent> getPlayerBean(player: Player, type: KClass<T>): T {
    val scope = PlayerScopeHandler.scopes[player.uniqueId]
        ?: throw IllegalStateException("PlayerScope for ${player.name} is missing")

    return scope.getBeanOfType(type)?.instance
        ?: throw IllegalArgumentException("${type.jvmName} not found in PlayerScope")
}