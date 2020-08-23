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

import java.lang.reflect.Method
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass

/**
 * Get method of [R] named [name] with return type [T] and parameter [params]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
public inline fun <reified R : Any, reified T> methodOf(
    name: String,
    vararg params: KClass<*>
): Method {
    val method = R::class.java.getDeclaredMethod(name, *params.map { it.java }.toTypedArray())
    if (method.returnType != T::class.java)
        throw IllegalArgumentException("Return type mismatch: "
            + "expect ${T::class.java.name}, but ${method.returnType}")

    return method
}

/**
 * Get [MethodAccess0] represent method of [R] named [name] with return type [T]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
public inline fun <reified R : Any, reified T>
    methodDelegate0(name: String): ReadOnlyProperty<R, MethodAccess0<R, T>> = ReadOnlyProperty { thisRef, _ -> MethodAccess0(thisRef, methodOf<R, T>(name)) }

/**
 * Get [MethodAccess1] represent method of [R] named [name] with return type [T] and parameter [P1]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
public inline fun <reified R : Any, reified T, reified P1>
    methodDelegate1(name: String): ReadOnlyProperty<R, MethodAccess1<R, T, P1>> = ReadOnlyProperty { thisRef, _ ->
    MethodAccess1(thisRef, methodOf<R, T>(name, P1::class.toPrimitive()))
}

/**
 * Get [MethodAccess2] represent method of [R] named [name] with return type [T] and parameter [P1],[P2]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
public inline fun <reified R : Any, reified T, reified P1, reified P2>
    methodDelegate2(name: String): ReadOnlyProperty<R, MethodAccess2<R, T, P1, P2>> = ReadOnlyProperty { thisRef, _ ->
    MethodAccess2(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive()))
}

/**
 * Get [MethodAccess3] represent method of [R] named [name] with return type [T] and parameter [P1],[P2],[P3]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
public inline fun <reified R : Any, reified T, reified P1, reified P2, reified P3>
    methodDelegate3(name: String): ReadOnlyProperty<R, MethodAccess3<R, T, P1, P2, P3>> = ReadOnlyProperty { thisRef, _ ->
    MethodAccess3(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive(), P3::class.toPrimitive()))
}

/**
 * Get [MethodAccess4] represent method of [R] named [name] with return type [T] and parameter [P1],[P2],[P3],[P4]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
public inline fun <reified R : Any, reified T, reified P1, reified P2, reified P3, reified P4>
    methodDelegate4(name: String): ReadOnlyProperty<R, MethodAccess4<R, T, P1, P2, P3, P4>> = ReadOnlyProperty { thisRef, _ ->
    MethodAccess4(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive(), P3::class.toPrimitive(), P4::class.toPrimitive()))
}

/**
 * Get [MethodAccess5] represent method of [R] named [name] with return type [T] and parameter [P1],[P2],[P3],[P4],[P5]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
public inline fun <reified R : Any, reified T, reified P1, reified P2, reified P3, reified P4, reified P5>
    methodDelegate5(name: String): ReadOnlyProperty<R, MethodAccess5<R, T, P1, P2, P3, P4, P5>> = ReadOnlyProperty { thisRef, _ ->
    MethodAccess5(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive(), P3::class.toPrimitive(), P4::class.toPrimitive(), P5::class.toPrimitive()))
}

/**
 * Get [MethodAccess6] represent method of [R] named [name] with return type [T] and parameter [P1],[P2],[P3],[P4],[P5],[P6]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
public inline fun <reified R : Any, reified T, reified P1, reified P2, reified P3, reified P4, reified P5, reified P6>
    methodDelegate6(name: String): ReadOnlyProperty<R, MethodAccess6<R, T, P1, P2, P3, P4, P5, P6>> = ReadOnlyProperty { thisRef, _ ->
    MethodAccess6(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive(), P3::class.toPrimitive(), P4::class.toPrimitive(), P5::class.toPrimitive(), P6::class.toPrimitive()))
}

/**
 * Get [MethodAccess7] represent method of [R] named [name] with return type [T] and parameter [P1],[P2],[P3],[P4],[P5],[P6],[P7]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
public inline fun <reified R : Any, reified T, reified P1, reified P2, reified P3, reified P4, reified P5, reified P6, reified P7>
    methodDelegate7(name: String): ReadOnlyProperty<R, MethodAccess7<R, T, P1, P2, P3, P4, P5, P6, P7>> = ReadOnlyProperty { thisRef, _ ->
    MethodAccess7(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive(), P3::class.toPrimitive(), P4::class.toPrimitive(), P5::class.toPrimitive(), P6::class.toPrimitive(), P7::class.toPrimitive()))
}

/**
 * Get [MethodAccess8] represent method of [R] named [name] with return type [T] and parameter [P1],[P2],[P3],[P4],[P5],[P6],[P7],[P8]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
public inline fun <reified R : Any, reified T, reified P1, reified P2, reified P3, reified P4, reified P5, reified P6, reified P7, reified P8>
    methodDelegate8(name: String): ReadOnlyProperty<R, MethodAccess8<R, T, P1, P2, P3, P4, P5, P6, P7, P8>> = ReadOnlyProperty { thisRef, _ ->
    MethodAccess8(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive(), P3::class.toPrimitive(), P4::class.toPrimitive(), P5::class.toPrimitive(), P6::class.toPrimitive(), P7::class.toPrimitive(), P8::class.toPrimitive()))
}

/**
 * Get [MethodAccess9] represent method of [R] named [name] with return type [T] and parameter [P1],[P2],[P3],[P4],[P5],[P6],[P7],[P8],[P9]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
public inline fun <reified R : Any, reified T, reified P1, reified P2, reified P3, reified P4, reified P5, reified P6, reified P7, reified P8, reified P9>
    methodDelegate9(name: String): ReadOnlyProperty<R, MethodAccess9<R, T, P1, P2, P3, P4, P5, P6, P7, P8, P9>> = ReadOnlyProperty { thisRef, _ ->
    MethodAccess9(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive(), P3::class.toPrimitive(), P4::class.toPrimitive(), P5::class.toPrimitive(), P6::class.toPrimitive(), P7::class.toPrimitive(), P8::class.toPrimitive(), P9::class.toPrimitive()))
}