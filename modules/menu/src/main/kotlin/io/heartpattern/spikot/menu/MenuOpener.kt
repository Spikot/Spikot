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

package io.heartpattern.spikot.menu

import io.heartpattern.spikot.SpikotPlugin
import io.heartpattern.spikot.bean.definition.BeanDefinition
import io.heartpattern.spikot.bean.definition.DefaultAnnotationBeanDefinition
import io.heartpattern.spikot.player.PlayerScopeHandler
import org.bukkit.entity.Player
import kotlin.reflect.KClass

private fun Player.openMenu(plugin: SpikotPlugin, definition: BeanDefinition) {
    val registry = PlayerScopeHandler[plugin]!![this]!!
    val instance = definition.create(registry) as Menu
    instance.doOpen(definition, registry)
}

public fun Player.openMenu(plugin: SpikotPlugin, type: KClass<out Menu>) {
    openMenu(plugin, DefaultAnnotationBeanDefinition.fromClass(type))
}

public inline fun <reified T : Menu> Player.openMenu(plugin: SpikotPlugin) {
    openMenu(plugin, T::class)
}

public fun Player.openMenu(plugin: SpikotPlugin, menu: Menu) {
    openMenu(plugin, DefaultAnnotationBeanDefinition.fromProvider(menu::class) { menu })
}