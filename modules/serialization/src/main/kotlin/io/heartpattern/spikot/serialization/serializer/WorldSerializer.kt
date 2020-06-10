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

package io.heartpattern.spikot.serialization.serializer

import kotlinx.serialization.*
import org.bukkit.Bukkit
import org.bukkit.World
import java.util.*

object WorldSerializer : KSerializer<World> {
    override val descriptor: SerialDescriptor = PrimitiveDescriptor("org.bukkit.World", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: World) {
        encoder.encodeString(value.uid.toString())
    }

    override fun deserialize(decoder: Decoder): World {
        return Bukkit.getWorld(UUID.fromString(decoder.decodeString()))!!
    }
}