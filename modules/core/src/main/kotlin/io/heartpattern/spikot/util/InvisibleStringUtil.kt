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

import kotlin.experimental.or

private const val dictionary = "abcdefghijlmnopqstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$"
private const val special = '§'
private const val seperator = "§r"

/**
 * Encrypt given [byteArray] to invisible text in minecraft
 */
public fun encryptInvisibleString(byteArray: ByteArray): String {
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
public fun decryptInvisibleString(text: String): ByteArray {
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
public fun String.attachInvisibleString(byteArray: ByteArray): String {
    return this + "§r" + encryptInvisibleString(byteArray)
}

/**
 * Remove invisible text from [this]
 */
public fun String.detachInvisibleString(): String {
    return this.substringBeforeLast("§r")
}

/**
 * Get invisible value from [this]
 */
public fun String.getInvisibleValue(): ByteArray {
    return decryptInvisibleString(this.substringAfterLast("§r"))
}