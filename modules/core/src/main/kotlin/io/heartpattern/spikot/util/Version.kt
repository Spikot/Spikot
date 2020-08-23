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

package io.heartpattern.spikot.util

import org.bukkit.Bukkit
import java.util.regex.Pattern

public val MINECRAFT_VERSION: Version = Version.fromString(Bukkit.getVersion().substringAfter("(MC: ").substringBefore(")"))

public data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int
) : Comparable<Version> {
    override fun compareTo(other: Version): Int {
        return when {
            major != other.major -> major - other.major
            minor != other.minor -> minor - other.minor
            else -> patch - other.patch
        }
    }

    override fun toString(): String = "$major.$minor.$patch"

    public companion object {
        private val versionRegex = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)")

        @JvmStatic
        public fun fromString(string: String): Version {
            val parsed = versionRegex.matcher(string)
            parsed.matches()
            return Version(
                parsed.group(1).toInt(),
                parsed.group(2).toInt(),
                parsed.group(3).toInt()
            )
        }
    }
}