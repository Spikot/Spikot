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

package io.heartpattern.spikot.reflection

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private class FieldTarget {
    private val field: String = "str"
}

class FieldAccessTest {
    private var FieldTarget.fieldDelegate by fieldDelegate<FieldTarget, String>("field")

    @Test
    fun fieldOf() {
        val field = fieldOf<FieldTarget, String>("field")
        assertEquals("field", field.name)
        assertEquals(String::class.java, field.type)
        assertEquals(FieldTarget::class.java, field.declaringClass)
    }

    @Test
    fun `fieldOf throw test`() {
        assertThrows<IllegalArgumentException> {
            fieldOf<FieldTarget, Float>("field")
        }
    }

    @Test
    fun fieldDelegate() {
        val target = FieldTarget()
        assertEquals("str", target.fieldDelegate)
        target.fieldDelegate = "aaa"
        assertEquals("aaa", target.fieldDelegate)
    }
}