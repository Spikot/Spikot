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

package io.heartpattern.spikot.advancement.v12

import io.heartpattern.spikot.ResourceLocation
import io.heartpattern.spikot.adapter.Adapter
import io.heartpattern.spikot.advancement.AdvancementAdapter
import io.heartpattern.spikot.advancement.model.*
import io.heartpattern.spikot.advancement.model.Advancement
import io.heartpattern.spikot.advancement.model.AdvancementDisplay
import io.heartpattern.spikot.advancement.model.AdvancementProgress
import io.heartpattern.spikot.advancement.model.AdvancementRewards
import io.heartpattern.spikot.advancement.model.criterion.AdvancementCriterion
import io.heartpattern.spikot.advancement.model.trigger.ImpossibleTrigger
import io.heartpattern.spikot.packet.sendPacket
import net.minecraft.server.v1_12_R1.*
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage
import org.bukkit.entity.Player
import net.minecraft.server.v1_12_R1.Advancement as NMSAdvancement
import net.minecraft.server.v1_12_R1.AdvancementDisplay as NMSAdvancementDisplay
import net.minecraft.server.v1_12_R1.AdvancementProgress as NMSAdvancementProgress
import net.minecraft.server.v1_12_R1.AdvancementRewards as NMSAdvancementRewards
import net.minecraft.server.v1_12_R1.Criterion as NMSCriterion
import net.minecraft.server.v1_12_R1.AdvancementFrameType as NMSAdvancementFrameType

@Adapter("1.12~1.12.2")
class AdvancementAdapterImpl : AdvancementAdapter {
    override fun sendAdvancementData(
        player: Player,
        reset: Boolean,
        add: AdvancementContainer,
        remove: Set<ResourceLocation>,
        progress: Map<ResourceLocation, AdvancementProgress>
    ) {
        val packet = PacketPlayOutAdvancements(
            reset,
            add.allAdvancements.map { it.toNMS() },
            remove.map { MinecraftKey(it.namespace, it.path) }.toMutableSet(),
            progress.map { (key, value) ->
                MinecraftKey(key.namespace, key.path) to value.toNMS()
            }.toMap()
        )

        player.sendPacket(packet)
    }

    private fun Advancement.toNMS(): NMSAdvancement {
        return NMSAdvancement(
            MinecraftKey(id.namespace, id.path),
            parent?.toNMS(),
            display?.toNMS(),
            rewards.toNMS(),
            criteria.mapValues { it.value.toNMS() },
            requirements.map { it.toTypedArray() }.toTypedArray()
        )
    }

    private fun AdvancementDisplay.toNMS(): NMSAdvancementDisplay {
        return NMSAdvancementDisplay(
            CraftItemStack.asNMSCopy(icon),
            CraftChatMessage.fromString(title)[0],
            CraftChatMessage.fromString(description)[0],
            background?.run { MinecraftKey(namespace, path) },
            when (frame) {
                AdvancementFrameType.TASK -> NMSAdvancementFrameType.TASK
                AdvancementFrameType.CHALLENGE -> NMSAdvancementFrameType.CHALLENGE
                AdvancementFrameType.GOAL -> NMSAdvancementFrameType.GOAL
            },
            showToast,
            announceToChat,
            hidden
        )
    }

    private fun AdvancementRewards.toNMS(): NMSAdvancementRewards {
        return NMSAdvancementRewards(
            experience,
            loot.map { MinecraftKey(it.namespace, it.path) }.toTypedArray(),
            recipes.map { MinecraftKey(it.namespace, it.path) }.toTypedArray(),
            CustomFunction.a.a
        )
    }

    private fun AdvancementProgress.toNMS(): NMSAdvancementProgress {
        return NMSAdvancementProgress().apply {
            a(
                advancement.criteria.mapValues { it.value.toNMS() },
                advancement.requirements.map { it.toTypedArray() }.toTypedArray()
            )
            progress.forEach { (k, v) ->
                if (v.isDone)
                    a(k)
            }
        }
    }

    private fun AdvancementCriterion.toNMS(): NMSCriterion {
        return NMSCriterion(
            when (trigger) {
                is ImpossibleTrigger -> CriterionTriggerImpossible.a()
                else -> throw IllegalArgumentException("")
            }
        )
    }
}