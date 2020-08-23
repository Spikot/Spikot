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

package io.heartpattern.spikot.extension

import org.bukkit.plugin.Plugin
import java.io.File

/**
 * Get [file] in [path] inside plugin's data folder
 */
public fun Plugin.getFile(path: String, file: String): File {
    return File(File(dataFolder, path), file)
}

/**
 * Get [file] inside plugin's data folder
 */
public fun Plugin.getFile(file: String): File {
    return File(dataFolder, file)
}

/**
 * Get [file] in [path] and create it inside plugin's data folder
 * @return File in plugin's data folder
 */
public fun Plugin.createFile(path: String, file: String): File {
    val dir = File(dataFolder, path)
    if (!dir.exists()) {
        dir.mkdirs()
    }
    val final = File(dir, file)
    final.createNewFile()
    return final
}


/**
 * Get [file] and create it inside plugin's data folder
 */
public fun Plugin.createFile(file: String): File {
    if (!dataFolder.exists()) {
        dataFolder.mkdirs()
    }
    val final = File(dataFolder, file)
    final.createNewFile()
    return final
}