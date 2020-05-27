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

package io.heartpattern.spikot.test.chat

import io.heartpattern.spikot.chat.chat
import io.heartpattern.spikot.chat.sendMessage
import io.heartpattern.spikot.component.scopes.player.PlayerComponent
import io.heartpattern.spikot.component.scopes.player.PlayerScope
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack


@PlayerScope
class ChatTestComponent : PlayerComponent() {
    override fun onEnable() {
        val item = ItemStack(Material.STONE, 5).apply {
            itemMeta = itemMeta.apply {
                displayName = "DisplayName"
                lore = listOf("Lore1", "Lore2")
                isUnbreakable = true
            }
            addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3)
        }
        player.sendMessage(chat.blue.underline.runCommand("/help").hoverItem(item)("Item").resetAll("RESET"))
        player.sendMessage(chat.blue.underline.openUrl("https://google.com/").hoverText(chat.blue("Google"))("Browser"))
        player.sendMessage(chat.blue.underline.hoverEntity(player)("Entity"))
    }
}