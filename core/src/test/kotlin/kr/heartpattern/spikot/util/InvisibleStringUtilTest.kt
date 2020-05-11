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

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class InvisibleStringUtilTest {
    @Test
    fun encryptDecryptTest() {
        repeat(100) {
            val byteArray = Random.nextBytes(Random.nextInt(100))
            val encrypted = encryptInvisibleString(byteArray)
            val decrypted = decryptInvisibleString(encrypted)
            assertArrayEquals(byteArray, decrypted)
        }
    }

    @Test
    fun encryptDecryptAttachTest() {
        repeat(100) {
            val string = "§aSome§rString§k"
            val byteArray = Random.nextBytes(Random.nextInt(100))
            val encrypted = string.attachInvisibleString(byteArray)
            val decrypted = encrypted.getInvisibleValue()
            assertEquals(string, encrypted.detachInvisibleString())
            assertArrayEquals(byteArray, decrypted)
        }
    }
}