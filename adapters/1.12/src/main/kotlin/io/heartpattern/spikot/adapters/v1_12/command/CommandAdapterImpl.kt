package io.heartpattern.spikot.adapters.v1_12.command

import io.heartpattern.spikot.adapter.Adapter
import io.heartpattern.spikot.command.CommandAdapter
import io.heartpattern.spikot.Component
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.craftbukkit.v1_12_R1.CraftServer
import org.bukkit.plugin.Plugin

@Adapter(adapterOf = CommandAdapter::class, version = "1.12.2")
object CommandAdapterImpl : Component(), CommandAdapter {
    private val commandMap = (Bukkit.getServer() as CraftServer).commandMap

    override fun registerCommand(plugin: Plugin, command: Command) {
        commandMap.register(plugin.name, command)
    }

    override fun unregisterCommand(command: Command) {
        command.unregister(commandMap)
    }
}