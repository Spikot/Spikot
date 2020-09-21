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

import com.charleskorn.kaml.Yaml
import io.heartpattern.spikot.util.serverDir
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
private data class SpikotConfiguration(
    val profiles: List<String> = emptyList()
)

private val spikotConfiguration: SpikotConfiguration by lazy {
    val configurationFile = File(serverDir, "spikot.yml")
    if (!configurationFile.exists()) {
        configurationFile.createNewFile()
        val defaultConfiguration = SpikotConfiguration()
        val encoded = Yaml.default.encodeToString(SpikotConfiguration.serializer(), defaultConfiguration)
        configurationFile.writeText(encoded)
        defaultConfiguration
    } else {
        val encoded = configurationFile.readText()
        Yaml.default.decodeFromString(SpikotConfiguration.serializer(), encoded)
    }
}

public val activeProfiles: List<String> by lazy {
    spikotConfiguration.profiles + ((System.getProperty("spikot.profiles.active")
        ?: System.getenv("spikot.propfiles.active"))?.split(',') ?: emptyList())
}