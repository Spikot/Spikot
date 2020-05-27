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

import java.lang.reflect.Method
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Get method of [R] named [name] with return type [T] and parameter [params]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
inline fun <reified R : Any, reified T> methodOf(
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
inline fun <reified R : Any, reified T> methodDelegate0(name: String) = object : ReadOnlyProperty<R, MethodAccess0<R, T>> {
    override fun getValue(thisRef: R, property: KProperty<*>): MethodAccess0<R, T> {
        return MethodAccess0(thisRef, methodOf<R, T>(name))
    }
}

/**
 * Get [MethodAccess1] represent method of [R] named [name] with return type [T] and parameter [P1]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
inline fun <reified R : Any, reified T, reified P1> methodDelegate1(name: String) = object : ReadOnlyProperty<R, MethodAccess1<R, T, P1>> {
    override fun getValue(thisRef: R, property: KProperty<*>): MethodAccess1<R, T, P1> {
        return MethodAccess1(thisRef, methodOf<R, T>(name, P1::class.toPrimitive()))
    }
}

/**
 * Get [MethodAccess2] represent method of [R] named [name] with return type [T] and parameter [P1],[P2]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
inline fun <reified R : Any, reified T, reified P1, reified P2> methodDelegate2(name: String) = object : ReadOnlyProperty<R, MethodAccess2<R, T, P1, P2>> {
    override fun getValue(thisRef: R, property: KProperty<*>): MethodAccess2<R, T, P1, P2> {
        return MethodAccess2(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive()))
    }
}

/**
 * Get [MethodAccess3] represent method of [R] named [name] with return type [T] and parameter [P1],[P2],[P3]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
inline fun <reified R : Any, reified T, reified P1, reified P2, reified P3> methodDelegate3(name: String) = object : ReadOnlyProperty<R, MethodAccess3<R, T, P1, P2, P3>> {
    override fun getValue(thisRef: R, property: KProperty<*>): MethodAccess3<R, T, P1, P2, P3> {
        return MethodAccess3(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive(), P3::class.toPrimitive()))
    }
}

/**
 * Get [MethodAccess4] represent method of [R] named [name] with return type [T] and parameter [P1],[P2],[P3],[P4]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
inline fun <reified R : Any, reified T, reified P1, reified P2, reified P3, reified P4> methodDelegate4(name: String) = object : ReadOnlyProperty<R, MethodAccess4<R, T, P1, P2, P3, P4>> {
    override fun getValue(thisRef: R, property: KProperty<*>): MethodAccess4<R, T, P1, P2, P3, P4> {
        return MethodAccess4(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive(), P3::class.toPrimitive(), P4::class.toPrimitive()))
    }
}

/**
 * Get [MethodAccess5] represent method of [R] named [name] with return type [T] and parameter [P1],[P2],[P3],[P4],[P5]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
inline fun <reified R : Any, reified T, reified P1, reified P2, reified P3, reified P4, reified P5> methodDelegate5(name: String) = object : ReadOnlyProperty<R, MethodAccess5<R, T, P1, P2, P3, P4, P5>> {
    override fun getValue(thisRef: R, property: KProperty<*>): MethodAccess5<R, T, P1, P2, P3, P4, P5> {
        return MethodAccess5(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive(), P3::class.toPrimitive(), P4::class.toPrimitive(), P5::class.toPrimitive()))
    }
}

/**
 * Get [MethodAccess6] represent method of [R] named [name] with return type [T] and parameter [P1],[P2],[P3],[P4],[P5],[P6]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
inline fun <reified R : Any, reified T, reified P1, reified P2, reified P3, reified P4, reified P5, reified P6> methodDelegate6(name: String) = object : ReadOnlyProperty<R, MethodAccess6<R, T, P1, P2, P3, P4, P5, P6>> {
    override fun getValue(thisRef: R, property: KProperty<*>): MethodAccess6<R, T, P1, P2, P3, P4, P5, P6> {
        return MethodAccess6(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive(), P3::class.toPrimitive(), P4::class.toPrimitive(), P5::class.toPrimitive(), P6::class.toPrimitive()))
    }
}

/**
 * Get [MethodAccess7] represent method of [R] named [name] with return type [T] and parameter [P1],[P2],[P3],[P4],[P5],[P6],[P7]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
inline fun <reified R : Any, reified T, reified P1, reified P2, reified P3, reified P4, reified P5, reified P6, reified P7> methodDelegate7(name: String) = object : ReadOnlyProperty<R, MethodAccess7<R, T, P1, P2, P3, P4, P5, P6, P7>> {
    override fun getValue(thisRef: R, property: KProperty<*>): MethodAccess7<R, T, P1, P2, P3, P4, P5, P6, P7> {
        return MethodAccess7(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive(), P3::class.toPrimitive(), P4::class.toPrimitive(), P5::class.toPrimitive(), P6::class.toPrimitive(), P7::class.toPrimitive()))
    }
}

/**
 * Get [MethodAccess8] represent method of [R] named [name] with return type [T] and parameter [P1],[P2],[P3],[P4],[P5],[P6],[P7],[P8]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
inline fun <reified R : Any, reified T, reified P1, reified P2, reified P3, reified P4, reified P5, reified P6, reified P7, reified P8> methodDelegate8(name: String) = object : ReadOnlyProperty<R, MethodAccess8<R, T, P1, P2, P3, P4, P5, P6, P7, P8>> {
    override fun getValue(thisRef: R, property: KProperty<*>): MethodAccess8<R, T, P1, P2, P3, P4, P5, P6, P7, P8> {
        return MethodAccess8(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive(), P3::class.toPrimitive(), P4::class.toPrimitive(), P5::class.toPrimitive(), P6::class.toPrimitive(), P7::class.toPrimitive(), P8::class.toPrimitive()))
    }
}

/**
 * Get [MethodAccess9] represent method of [R] named [name] with return type [T] and parameter [P1],[P2],[P3],[P4],[P5],[P6],[P7],[P8],[P9]
 * @throws IllegalArgumentException If method does not exists or return type mismatch
 */
inline fun <reified R : Any, reified T, reified P1, reified P2, reified P3, reified P4, reified P5, reified P6, reified P7, reified P8, reified P9> methodDelegate9(name: String) = object : ReadOnlyProperty<R, MethodAccess9<R, T, P1, P2, P3, P4, P5, P6, P7, P8, P9>> {
    override fun getValue(thisRef: R, property: KProperty<*>): MethodAccess9<R, T, P1, P2, P3, P4, P5, P6, P7, P8, P9> {
        return MethodAccess9(thisRef, methodOf<R, T>(name, P1::class.toPrimitive(), P2::class.toPrimitive(), P3::class.toPrimitive(), P4::class.toPrimitive(), P5::class.toPrimitive(), P6::class.toPrimitive(), P7::class.toPrimitive(), P8::class.toPrimitive(), P9::class.toPrimitive()))
    }
}