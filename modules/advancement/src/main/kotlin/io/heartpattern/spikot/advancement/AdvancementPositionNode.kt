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

import io.heartpattern.spikot.advancement.model.Advancement
import io.heartpattern.spikot.advancement.model.AdvancementContainer

// TODO
@ExperimentalStdlibApi
internal class AdvancementPositionNode private constructor(
    container: AdvancementContainer,
    val advancement: Advancement,
    val parent: AdvancementPositionNode?,
    val previousSibling: AdvancementPositionNode?,
    val childIndex: Int,
    private var x: Int
) {
    private var y: Float = -1f
    private var mod: Float = 0f
    private var change: Float = 0f
    private var shift: Float = 0f
    private var ancestor: AdvancementPositionNode? = this
    private var thread: AdvancementPositionNode? = null
    private val children: List<AdvancementPositionNode>

    init {
        children = buildList {
            fun addChild(adv: Advancement, previous: AdvancementPositionNode?): AdvancementPositionNode? {
                var prev = previous
                if (adv.display != null) {
                    prev = AdvancementPositionNode(container, adv, this@AdvancementPositionNode, previous, size + 1, x + 1)
                    add(prev)
                } else {
                    container.getChildren(adv).forEach { child ->
                        prev = addChild(child, prev)
                    }
                }

                return prev
            }

            var previousSib: AdvancementPositionNode? = null

            for (child in container.getChildren(advancement)) {
                previousSib = addChild(child, previousSib)
            }
        }
    }

    private fun firstPhase() {
        if (children.isEmpty()) {
            y = if (previousSibling != null) previousSibling.y + 1.0f else 0f
        } else {
//            val node1: AdvancementPositionNode? = null
//            val node2: AdvancementPositionNode? = null

        }
    }

    // second phase

    // third phase

    private fun executeShifts() {
        var dy = 0f
        var dmod = 0f

        children.reversed().forEach { node ->
            node.y += dy
            node.mod += dmod
            dmod += node.change
            dy += node.shift + dy
        }
    }

    private fun previousOrThread(): AdvancementPositionNode? {
        return thread ?: if (children.isEmpty()) null else children.first()
    }

    private fun nextOrThread(): AdvancementPositionNode? {
        return thread ?: if (children.isEmpty()) null else children.last()
    }

    private fun apportion(root: AdvancementPositionNode): AdvancementPositionNode {
        if (previousSibling == null) {
            return root
        } else {
            var node2 = this
            var node3 = this
            var node4: AdvancementPositionNode = previousSibling
            var node5 = parent!!.children[0]
            var f1 = mod
            var f2 = mod
            var f3 = node4.mod

            var f4 = 0f
            while (node4.nextOrThread() != null && node2.previousOrThread() != null) {
                f4 = node5.mod
                node2 = node2.previousOrThread()!!
                node3 = node3.nextOrThread()!!
                node4 = node4.nextOrThread()!!
                node5 = node5.previousOrThread()!!
                node3.ancestor = this
                val f5 = node4.y + f3 - (node2.y + f1) + 1f
                if (f5 > 0f) {
                    node4.getAncestor(this, root).moveSubtree(this, f5)
                    f1 += f5
                    f2 += f5
                }

                f3 += node4.mod
                f1 += node2.mod
                f4 += node5.mod
                f2 += node3.mod
            }

            if (node4.nextOrThread() != null && node3.nextOrThread() == null) {
                node3.thread = node4.nextOrThread()
                node3.mod += f3 - f2
            } else {
                if (node4.previousOrThread() != null && node5.previousOrThread() == null) {
                    node5.thread = node2.previousOrThread()
                    node5.mod += f1 - f4
                }

                return this
            }

            return root
        }
    }

    private fun moveSubtree(node: AdvancementPositionNode, height: Float) {
        val dIndex = (node.childIndex - childIndex).toFloat()
        if (dIndex != 0f) {
            node.change -= height / dIndex
            change += height / dIndex
        }

        node.shift += height
        node.y += height
        node.mod += height
    }

    fun getAncestor(a: AdvancementPositionNode, b: AdvancementPositionNode): AdvancementPositionNode {
        return if (ancestor != null && a.parent!!.children.contains(ancestor)) ancestor!! else b
    }

    private fun finalizePosition() {
        // TODO
    }
}