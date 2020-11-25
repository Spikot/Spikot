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

package io.heartpattern.spikot.test

import io.heartpattern.spikot.ResourceLocation
import io.heartpattern.spikot.advancement.model.*
import io.heartpattern.spikot.advancement.model.criterion.AdvancementCriterion
import io.heartpattern.spikot.advancement.model.criterion.AdvancementCriterionProgress
import io.heartpattern.spikot.advancement.model.trigger.ImpossibleTrigger
import io.heartpattern.spikot.advancement.sendAdvancementMessage
import io.heartpattern.spikot.advancement.updateAdvancement
import io.heartpattern.spikot.command.AdvancedCommandExecutor
import io.heartpattern.spikot.command.CommandComponent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.time.LocalDateTime

@CommandComponent("test_achievement")
class TestAchievementComponent : AdvancedCommandExecutor() {
    private val root = Advancement(
        ResourceLocation("test", "root"),
        null,
        AdvancementDisplay(
            "Root",
            "This is root",
            ItemStack(Material.ACACIA_BOAT),
            AdvancementFrameType.CHALLENGE,
            null,
            true,
            true,
            false,
            AdvancementPosition(0f, 0f)
        ),
        mapOf("imp" to AdvancementCriterion(ImpossibleTrigger)),
        listOf(listOf("imp")),
        AdvancementRewards(emptyList(), emptyList(), 0)
    )

    override fun CommandExecutorContext.onCommand() {
        if (args[0] == "reset") {
            (sender as Player).updateAdvancement(
                true,
                setOf(AdvancementContainer.of(this@TestAchievementComponent.root)),
                emptySet(),
                mapOf()
            )
        } else if (args[0] == "notify") {
            (sender as Player).sendAdvancementMessage(
                ItemStack(Material.ENDER_EYE),
                "Test notification",
                AdvancementFrameType.GOAL
            )
        } else {
            (sender as Player).updateAdvancement(
                false,
                setOf(AdvancementContainer()),
                emptySet(),
                mapOf(
                    ResourceLocation("test", "root") to AdvancementProgress(
                        root,
                        mapOf("imp" to AdvancementCriterionProgress.Complete(LocalDateTime.now()))
                    )
                )
            )
        }
    }
}