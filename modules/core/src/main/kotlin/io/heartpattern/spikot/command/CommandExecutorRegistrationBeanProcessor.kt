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

package io.heartpattern.spikot.command

import io.heartpattern.spikot.bean.BeanProcessor
import io.heartpattern.spikot.bean.BeanProcessorContext
import io.heartpattern.spikot.bean.Component
import io.heartpattern.spikot.bean.definition.BeanDefinition
import mu.KotlinLogging
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter

private val logger = KotlinLogging.logger {}

@Component
private class CommandExecutorRegistrationBeanProcessor : BeanProcessor {
    override fun beforeInitialize(context: BeanProcessorContext, definition: BeanDefinition, bean: Any) {
        val command = definition.annotations.get<CommandComponent>()?.synthesize()
            ?: return

        val name = command.name

        val pluginCommand = Bukkit.getPluginCommand(name)

        if (pluginCommand == null) {
            logger.error { "Cannot register command executor $definition" }
            return
        }

        if (bean is CommandExecutor)
            pluginCommand.setExecutor(bean)

        if (bean is TabCompleter)
            pluginCommand.tabCompleter = bean
    }

    override fun afterDestroy(context: BeanProcessorContext, definition: BeanDefinition, bean: Any) {
        val command = definition.annotations.get<CommandComponent>()?.synthesize()
            ?: return

        val name = command.name

        val pluginCommand = Bukkit.getPluginCommand(name) ?: return

        if (bean is CommandExecutor)
            pluginCommand.setExecutor(null)

        if (bean is TabCompleter)
            pluginCommand.tabCompleter = null
    }
}