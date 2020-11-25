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

package io.heartpattern.spikot.advancement

import io.heartpattern.spikot.ResourceLocation
import io.heartpattern.spikot.advancement.model.*
import io.heartpattern.spikot.advancement.model.criterion.AdvancementCriterion
import io.heartpattern.spikot.advancement.model.criterion.AdvancementCriterionProgress
import io.heartpattern.spikot.advancement.model.trigger.ImpossibleTrigger
import io.heartpattern.spikot.util.randomString
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.time.LocalDateTime

public fun Player.updateAdvancement(
    reset: Boolean = false,
    add: Collection<AdvancementContainer> = emptySet(),
    remove: Collection<ResourceLocation> = emptySet(),
    progress: Map<ResourceLocation, AdvancementProgress> = emptyMap()
) {
    AdvancementAdapter.sendAdvancementData(this, reset, add, remove, progress)
}

public fun Player.sendAdvancementMessage(icon: ItemStack, title: String, type: AdvancementFrameType) {
    val id = ResourceLocation("spikot", randomString(64))
    val advancement = Advancement(
        id,
        null,
        AdvancementDisplay(
            title,
            "",
            icon,
            type,
            null,
            showToast = true,
            announceToChat = false,
            hidden = true,
            position = AdvancementPosition(0f, 0f)
        ),
        mapOf("IMP" to AdvancementCriterion(ImpossibleTrigger)),
        listOf(listOf("IMP")),
        AdvancementRewards.EMPTY
    )
    updateAdvancement(
        add = setOf(AdvancementContainer.of(advancement)),
        progress = mapOf(
            id to AdvancementProgress(advancement, mapOf("IMP" to AdvancementCriterionProgress.Complete(LocalDateTime.now())))
        )
    )
}