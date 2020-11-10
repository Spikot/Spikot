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

package io.heartpattern.spikot.nbt

import io.heartpattern.spikot.Spikot
import io.heartpattern.spikot.adapter.adapterOf
import java.io.DataInputStream
import java.io.DataOutput
import java.io.InputStream
import java.io.OutputStream

public interface NBTIOAdapter {
    public fun readCompressed(input: InputStream): CompoundTag
    public fun writeCompressed(tag: CompoundTag, output: OutputStream)
    public fun read(input: DataInputStream): CompoundTag
    public fun write(tag: CompoundTag, output: DataOutput)

    public companion object Impl : NBTIOAdapter by adapterOf<Spikot, NBTIOAdapter>()
}