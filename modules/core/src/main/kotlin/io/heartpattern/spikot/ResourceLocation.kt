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

package io.heartpattern.spikot

import org.bukkit.NamespacedKey
import org.bukkit.plugin.Plugin
import java.util.*

private val namespacePattern = Regex("[a-z0-9._-]+")
private val idPattern = Regex("[a-z0-9/._-]+")

public data class ResourceLocation(
    val namespace: String,
    val path: String
){
    public constructor(plugin: Plugin, id: String): this(plugin.name.toLowerCase(Locale.ROOT), id)

    init{
        check(namespacePattern.matches(namespace)){
            "Illegal namespace. Must be [a-z0-9._-]: $namespace"
        }
        check(idPattern.matches(path)){
            "Illegal id. Must be [a-z0-9/._-]: $path"
        }
        check(namespace.length + path.length + 1 < 256){
            "NamespaceId must be less than 256 characters: $namespace:$path"
        }
    }

    public fun toNamespacedKey(): NamespacedKey{
        @Suppress("DEPRECATION")
        return NamespacedKey(namespace, path)
    }

    override fun toString(): String = "$namespace:$path"

    public companion object{
        public fun minecraft(id: String): ResourceLocation{
            return ResourceLocation("minecraft", id)
        }
    }
}