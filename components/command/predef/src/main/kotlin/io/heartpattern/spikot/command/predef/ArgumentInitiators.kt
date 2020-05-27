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

package io.heartpattern.spikot.command.predef

import io.heartpattern.spikot.chat.chat
import io.heartpattern.spikot.command.CommandDSL
import io.heartpattern.spikot.command.argument.CommandArgumentBuilder
import io.heartpattern.spikot.command.failure.TooFewArgumentFailure
import io.heartpattern.spikot.type.Left
import io.heartpattern.spikot.type.right
import io.heartpattern.spikot.type.tryEither
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player

val CommandDSL.string: CommandArgumentBuilder<String>
    get() = single

val CommandDSL.single: CommandArgumentBuilder<String>
    get() = argument.modify { _, source ->
        source.nextOrNull()?.right() ?: Left(TooFewArgumentFailure(source.pointer))
    }

fun CommandDSL.multiple(length: Int): CommandArgumentBuilder<List<String>> = argument.modify { _, source ->
    val list = ArrayList<String>(length)

    repeat(length) {
        list.add(source.nextOrNull() ?: return@modify Left(TooFewArgumentFailure(source.pointer)))
    }

    list.right()
}.setName(-10000, chat("strings"))

val CommandDSL.remain: CommandArgumentBuilder<List<String>>
    get() = argument.modify { ctx, source ->
        val list = ArrayList<String>(ctx.arguments.size - source.pointer)

        while (source.hasNext())
            list.add(source.next())

        list.right()
    }.setName(-10000, chat("strings"))

val CommandDSL.int: CommandArgumentBuilder<Int>
    get() = single.modify { _, source ->
        tryEither { source.toInt() }
    }.setName(-10000, chat("int"))

val CommandDSL.double: CommandArgumentBuilder<Double>
    get() = single.modify { _, source ->
        tryEither { source.toDouble() }
    }.setName(-10000, chat("double"))

val CommandDSL.player: CommandArgumentBuilder<Player>
    get() = single.modify { _, source ->
        Bukkit.getPlayerExact(source)?.right() ?: Left(PlayerNotFoundFailure(source))
    }.setName(-10000, chat("player"))
        .setCompleter(-10000) { _, _, _ -> Bukkit.getOnlinePlayers().map { it.name } }

val CommandDSL.material: CommandArgumentBuilder<Material>
    get() = single.modify { _, source ->
        val int = source.toIntOrNull()
        @Suppress("DEPRECATION")
        if (int != null)
            Material.getMaterial(int)?.right() ?: Left(NoSuchMaterialFailure(source))
        else
            Material.getMaterial(source.toUpperCase())?.right() ?: Left(NoSuchMaterialFailure(source))
    }.setCompleter(-10000) { _, _, _ -> Material.values().map { it.name } }

inline fun <reified T : Enum<T>> CommandDSL.enum(): CommandArgumentBuilder<T> {
    return single.modify { _, source ->
        val enum = T::class.java.enumConstants.find { it.name.toLowerCase() == source.toLowerCase() }
        enum?.right() ?: Left(NoSuchEnumConstantFailure(T::class, source))
    }
}