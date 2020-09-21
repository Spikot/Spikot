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

object Version {
    // Kotlins
    const val kotlin = "1.4.0"
    const val coroutine = "1.3.9"
    const val serialization = "1.0.0-RC"

    // MineCrafts
    val spigot = Spigot.V16.fullVersion

    // Tests
    const val junit_platform = "1.6.2"
    const val junit_jupyter = "5.6.2"
    const val mockk: String = "1.10.0"

    // Others
    const val reflections = "0.9.12"
    const val kotlin_logging = "1.7.8"
    const val slf4j = "1.7.30"
    const val auto_service = "1.0-rc7"
    const val guava = "29.0-jre"
    const val log4j2 = "2.8.1"
    const val kaml: String = "0.19.0"

    // Spigot versions
    enum class Spigot(val version: String, val fullVersion: String = "$version-R0.1-SNAPSHOT") {
        V16("1.16.2"),
        V15("1.15.2"),
        V14("1.14.4"),
        V13("1.13.2"),
        V12("1.12.2"),
        V11("1.11.2"),
        V10("1.10.2"),
        V9("1.9.4"),
        V8("1.8.8"),
        V7("1.7.10"),
        V6("1.6.4"),
        V5("1.5.2"),
        V4("1.4.7");

        companion object {
            operator fun get(version: Int) = valueOf("V$version")
        }
    }
}