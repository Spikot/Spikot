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

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class Option<out T> {
    companion object
}

data class Some<T>(val value: T) : Option<T>() {
    companion object
}

object None : Option<Nothing>()

@OptIn(ExperimentalContracts::class)
fun <T> Option<T>.isEmpty(): Boolean {
    contract {
        returns(true) implies (this@isEmpty is None)
        returns(false) implies (this@isEmpty is Some<T>)
    }
    return this is None
}

@OptIn(ExperimentalContracts::class)
fun <T> Option<T>.isNotEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotEmpty is Some<T>)
        returns(false) implies (this@isNotEmpty is None)
    }
    return this is Some
}

inline fun <T, R> Option<T>.fold(ifNone: () -> R, ifSome: (T) -> R): R = when (this) {
    is Some -> ifSome(value)
    is None -> ifNone()
}

fun <T> Option<T>.orNull(): T? = fold({ null }, { it })

fun <T> Option<T>.orDefault(default: T): T = fold({ default }, { it })

inline fun <T> Option<T>.orCompute(default: () -> T): T = fold(default, { it })

inline fun <T, R> Option<T>.map(map: (T) -> R): Option<R> = fold({ None }, { Some(map(it)) })

inline fun <T, R> Option<T>.flatMap(map: (T) -> Option<R>): Option<R> = fold({ None }, { map(it) })

inline fun <T> Option<T>.filter(filter: (T) -> Boolean): Option<T> = if (this is Some && filter(value)) this else None

fun <T : Any> T?.toOption(): Option<T> = if (this == null) None else Some(this)