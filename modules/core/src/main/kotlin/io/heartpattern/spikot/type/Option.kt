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

package io.heartpattern.spikot.type

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

public sealed class Option<out T> {
    public companion object
}

public data class Some<T>(val value: T) : Option<T>() {
    public companion object
}

public object None : Option<Nothing>()

@OptIn(ExperimentalContracts::class)
public fun <T> Option<T>.isEmpty(): Boolean {
    contract {
        returns(true) implies (this@isEmpty is None)
        returns(false) implies (this@isEmpty is Some<T>)
    }
    return this is None
}

@OptIn(ExperimentalContracts::class)
public fun <T> Option<T>.isNotEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotEmpty is Some<T>)
        returns(false) implies (this@isNotEmpty is None)
    }
    return this is Some
}

public inline fun <T, R> Option<T>.fold(ifNone: () -> R, ifSome: (T) -> R): R = when (this) {
    is Some -> ifSome(value)
    is None -> ifNone()
}

public fun <T> Option<T>.orNull(): T? = fold({ null }, { it })

public fun <T> Option<T>.orDefault(default: T): T = fold({ default }, { it })

public inline fun <T> Option<T>.orCompute(default: () -> T): T = fold(default, { it })

public inline fun <T, R> Option<T>.map(map: (T) -> R): Option<R> = fold({ None }, { Some(map(it)) })

public inline fun <T, R> Option<T>.flatMap(map: (T) -> Option<R>): Option<R> = fold({ None }, { map(it) })

public inline fun <T> Option<T>.filter(filter: (T) -> Boolean): Option<T> = if (this is Some && filter(value)) this else None

public fun <T : Any> T?.toOption(): Option<T> = if (this == null) None else Some(this)

public fun <T> T.wrapSome(): Some<T> = Some(this)