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

package io.heartpattern.spikot.annotation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.full.memberFunctions

@Retention(AnnotationRetention.RUNTIME)
annotation class BrokenSelfAlias(
    @get:AliasFor(attribute = "welcome")
    val greeting: String = "hello",
    @get:AliasFor(attribute = "greeting")
    val welcome: String = "hi"
)

@Retention(AnnotationRetention.RUNTIME)
annotation class RootAnnotation(
    @get:AliasFor(attribute = "welcome")
    val greeting: String = "hello",
    @get:AliasFor(attribute = "greeting")
    val welcome: String = "hello"
)

@Retention(AnnotationRetention.RUNTIME)
@RootAnnotation
annotation class MetaAnnotation(
    @get:AliasFor(annotation = RootAnnotation::class)
    val welcome: String
)

class AnnotatedMethods {
    @MetaAnnotation("hola")
    @RootAnnotation("root")
    fun legal() {
    }

    @BrokenSelfAlias
    fun illegal() {
    }
}

class MergedAnnotationTest {
    @Test
    fun legalMergedAnnotationTest() {
        val annotations = AnnotatedMethods::class.memberFunctions.first { it.name == "legal" }.mergedAnnotations
        val rootFirst = annotations.get<RootAnnotation>()!!.synthesize()
        assertEquals("root", rootFirst.greeting)
        assertEquals("root", rootFirst.welcome)
        val rootSecond = annotations.getAll<RootAnnotation>()[1].synthesize()
        assertEquals("hola", rootSecond.welcome)
        assertEquals("hola", rootSecond.greeting)
        val meta = annotations.get<MetaAnnotation>()!!.synthesize()
        assertEquals("hola", meta.welcome)
    }

    @Test
    fun illegalSelfAliasTest() {
        assertThrows<AnnotationConfigurationException> {
            AnnotatedMethods::class.memberFunctions.first { it.name == "illegal" }.mergedAnnotations
        }
    }
}