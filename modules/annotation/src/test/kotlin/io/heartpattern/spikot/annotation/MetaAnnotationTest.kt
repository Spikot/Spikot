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

package io.heartpattern.spikot.annotation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Retention(AnnotationRetention.RUNTIME)
private annotation class Target(val i: Int)

@Target(0)
@Retention(AnnotationRetention.RUNTIME)
private annotation class A

@A
@Target(1)
@Retention(AnnotationRetention.RUNTIME)
private annotation class B(@get:AliasFor(Target::class, "i") val c: Int)

@Target(2)
@A
@B(5)
class MyClass

@B(5)
class MyClass2

class MetaAnnotationTest {
    @Test
    fun checkMetaAnnotationSequence() {
        val expected = listOf(2, 0, 1)
        val annotations = MyClass::class.findMetaAnnotations<Target>().toList().map { it.annotation.i }

        assertEquals(expected, annotations)
    }

    @Test
    fun checkAliasFor() {
        val value = MyClass2::class.findMetaAnnotation<Target>()!!.getAttribute("i")
        assertEquals(5, value)
    }
}