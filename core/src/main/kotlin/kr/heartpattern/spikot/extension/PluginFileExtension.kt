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

package kr.heartpattern.spikot.extension

import org.bukkit.plugin.Plugin
import java.io.File

/**
 * Get [file] in [path] inside plugin's data folder
 */
fun Plugin.getFile(path: String, file: String): File {
    return File(File(dataFolder, path), file)
}

/**
 * Get [file] inside plugin's data folder
 */
fun Plugin.getFile(file: String): File {
    return File(dataFolder, file)
}

/**
 * Get [file] in [path] and create it inside data folder
 * @return File in plugin's data folder
 */
fun Plugin.createFile(path: String, file: String): File {
    val dir = File(dataFolder, path)
    if (!dir.exists()) {
        dir.mkdirs()
    }
    val final = File(dir, file)
    final.createNewFile()
    return final
}


/**
 * Get [file] and create it inside data folder
 */
fun Plugin.createFile(file: String): File {
    if (!dataFolder.exists()) {
        dataFolder.mkdirs()
    }
    val final = File(dataFolder, file)
    final.createNewFile()
    return final
}