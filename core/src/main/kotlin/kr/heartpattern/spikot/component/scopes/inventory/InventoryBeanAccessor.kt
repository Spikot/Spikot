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

package kr.heartpattern.spikot.component.scopes.inventory

import org.bukkit.entity.Player
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmName

/**
 * Provide access to [InventoryComponent]
 */
abstract class InventoryBeanAccessor<T : InventoryComponent>(private val type: KClass<T>) {
    operator fun get(player: Player): T? {
        return getInventoryBean(player, type)
    }
}

/**
 * Get inventory bean of type [T] for player of [this] component
 */
inline fun <reified T : InventoryComponent> InventoryComponent.inventoryBean(): Lazy<T> {
    return inventoryBean(T::class)
}

/**
 * Get inventory bean of [type] for player of [this] component
 */
fun <T : InventoryComponent> InventoryComponent.inventoryBean(type: KClass<T>): Lazy<T> {
    return lazy {
        getInventoryBean(player, type)
            ?: throw IllegalStateException("${type.jvmName} not found int InventoryScope")
    }
}

/**
 * Property delegate to get inventory bean typed [T]
 */
inline fun <reified T : InventoryComponent> inventoryBeanDelegate(): ReadOnlyProperty<Player, T?> {
    return inventoryBeanDelegate(T::class)
}

/**
 * Property delegate to get inventory bean typed [type]
 */
fun <T : InventoryComponent> inventoryBeanDelegate(type: KClass<T>): ReadOnlyProperty<Player, T?> {
    return object : ReadOnlyProperty<Player, T?> {
        override fun getValue(thisRef: Player, property: KProperty<*>): T? {
            return getInventoryBean(thisRef, type)
        }
    }
}

private fun <T : InventoryComponent> getInventoryBean(player: Player, type: KClass<T>): T? {
    return InventoryScopeHandler.scopes[player.uniqueId]?.getBeanOfType(type)?.instance
}