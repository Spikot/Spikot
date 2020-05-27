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

package io.heartpattern.spikot.nbt

import java.io.*

fun InputStream.readCompoundTag(): CompoundTag = NBTIOAdapter.readCompressed(this)
fun OutputStream.writeCompoundTag(tag: CompoundTag) = NBTIOAdapter.writeCompressed(tag, this)
fun DataInputStream.readCompoundTag(): CompoundTag = NBTIOAdapter.read(this)
fun DataOutput.writeCompoundTag(tag: CompoundTag) = NBTIOAdapter.write(tag, this)
fun ByteArray.parseCompoundTag(): CompoundTag = ByteArrayInputStream(this).readCompoundTag()
fun CompoundTag.toByteArray(): ByteArray = ByteArrayOutputStream().apply { writeCompoundTag(this@toByteArray) }.toByteArray()