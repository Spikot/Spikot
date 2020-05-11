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

package kr.heartpattern.spikot.util

import kotlin.experimental.or

private const val dictionary = "abcdefghijlmnopqstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$"
private const val special = '§'
private const val seperator = "§r"

/**
 * Encrypt given [byteArray] to invisible text in minecraft
 */
fun encryptInvisibleString(byteArray: ByteArray): String {
    val builder = StringBuilder()

    for (byte in byteArray) {
        val upper = (byte.toInt() shr 4) and 63
        val lower = byte.toInt() and 63
        builder.append('§').append(dictionary[upper])
        builder.append('§').append(dictionary[lower])
    }

    return builder.toString()
}

/**
 * Decrypt given [text] to ByteArray
 */
fun decryptInvisibleString(text: String): ByteArray {
    if (text.length % 4 != 0)
        throw IllegalArgumentException("Given test it not invisible text")
    val byteArray = ByteArray(text.length / 4)

    for (i in 0 until text.length / 4) {
        val upper = text[i * 4 + 1]
        val lower = text[i * 4 + 3]
        byteArray[i] = (dictionary.indexOf(upper) shl 4).toByte() or dictionary.indexOf(lower).toByte()
    }

    return byteArray
}

/**
 * Attach invisible text encrypting [byteArray] to [this]
 */
fun String.attachInvisibleString(byteArray: ByteArray): String {
    return this + "§r" + encryptInvisibleString(byteArray)
}

/**
 * Remove invisible text from [this]
 */
fun String.detachInvisibleString(): String {
    return this.substringBeforeLast("§r")
}

/**
 * Get invisible value from [this]
 */
fun String.getInvisibleValue(): ByteArray {
    return decryptInvisibleString(this.substringAfterLast("§r"))
}