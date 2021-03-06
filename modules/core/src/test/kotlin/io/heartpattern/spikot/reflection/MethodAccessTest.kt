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

package io.heartpattern.spikot.reflection

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private class MethodTarget(val name: String) {
    private fun function(): String {
        return "$name:function"
    }

    private fun function(p0: String): String {
        return "$name:function:$p0"
    }

    private fun function(p0: String, p1: Int): String {
        return "$name:function:$p0:$p1"
    }

    private fun function(p0: String, p1: Int, p2: Double): String {
        return "$name:function:$p0:$p1:$p2"
    }

    private fun function(p0: String, p1: Int, p2: Double, p3: Long): String {
        return "$name:function:$p0:$p1:$p2:$p3"
    }

    private fun function(p0: String, p1: Int, p2: Double, p3: Long, p4: Short): String {
        return "$name:function:$p0:$p1:$p2:$p3:$p4"
    }

    private fun function(p0: String, p1: Int, p2: Double, p3: Long, p4: Short, p5: Float): String {
        return "$name:function:$p0:$p1:$p2:$p3:$p4:$p5"
    }

    private fun function(p0: String, p1: Int, p2: Double, p3: Long, p4: Short, p5: Float, p6: Char): String {
        return "$name:function:$p0:$p1:$p2:$p3:$p4:$p5:$p6"
    }

    private fun function(p0: String, p1: Int, p2: Double, p3: Long, p4: Short, p5: Float, p6: Char, p7: Boolean): String {
        return "$name:function:$p0:$p1:$p2:$p3:$p4:$p5:$p6:$p7"
    }

    private fun function(p0: String, p1: Int, p2: Double, p3: Long, p4: Short, p5: Float, p6: Char, p7: Boolean, p8: String?): String {
        return "$name:function:$p0:$p1:$p2:$p3:$p4:$p5:$p6:$p7:$p8"
    }

    private fun test(p0: Int): String {
        return p0.toString()
    }
}

class MethodAccessTest {
    private val MethodTarget.function0 by methodDelegate0<MethodTarget, String>("function")
    private val MethodTarget.function1 by methodDelegate1<MethodTarget, String, String>("function")
    private val MethodTarget.function2 by methodDelegate2<MethodTarget, String, String, Int>("function")
    private val MethodTarget.function3 by methodDelegate3<MethodTarget, String, String, Int, Double>("function")
    private val MethodTarget.function4 by methodDelegate4<MethodTarget, String, String, Int, Double, Long>("function")
    private val MethodTarget.function5 by methodDelegate5<MethodTarget, String, String, Int, Double, Long, Short>("function")
    private val MethodTarget.function6 by methodDelegate6<MethodTarget, String, String, Int, Double, Long, Short, Float>("function")
    private val MethodTarget.function7 by methodDelegate7<MethodTarget, String, String, Int, Double, Long, Short, Float, Char>("function")
    private val MethodTarget.function8 by methodDelegate8<MethodTarget, String, String, Int, Double, Long, Short, Float, Char, Boolean>("function")
    private val MethodTarget.function9 by methodDelegate9<MethodTarget, String, String, Int, Double, Long, Short, Float, Char, Boolean, String?>("function")

    @Test
    fun methodOf() {
        val method = methodOf<MethodTarget, String>("function", String::class, Int::class)
        assertEquals("function", method.name)
        assertArrayEquals(arrayOf(String::class.java, Int::class.java), method.parameterTypes)
        assertEquals(String::class.java, method.returnType)
    }

    @Test
    fun `methodOf throw test`() {
        assertThrows<IllegalArgumentException> {
            methodOf<MethodTarget, List<String>>("function", String::class, Int::class)
        }
    }

    @Test
    fun methodDelegate() {
        val first = MethodTarget("first")
        val second = MethodTarget("second")
        assertEquals("first:function", first.function0())
        assertEquals("second:function", second.function0())
        assertEquals("first:function:str", first.function1("str"))
        assertEquals("first:function:str:99", first.function2("str", 99))
        assertEquals("first:function:str:99:2.1", first.function3("str", 99, 2.1))
        assertEquals("first:function:str:99:2.1:13", first.function4("str", 99, 2.1, 13L))
        assertEquals("first:function:str:99:2.1:13:3", first.function5("str", 99, 2.1, 13L, 3))
        assertEquals("first:function:str:99:2.1:13:3:9.8", first.function6("str", 99, 2.1, 13L, 3, 9.8f))
        assertEquals("first:function:str:99:2.1:13:3:9.8:g", first.function7("str", 99, 2.1, 13L, 3, 9.8f, 'g'))
        assertEquals("first:function:str:99:2.1:13:3:9.8:g:true", first.function8("str", 99, 2.1, 13L, 3, 9.8f, 'g', true))
        assertEquals("first:function:str:99:2.1:13:3:9.8:g:true:null", first.function9("str", 99, 2.1, 13L, 3, 9.8f, 'g', true, null))
    }
}