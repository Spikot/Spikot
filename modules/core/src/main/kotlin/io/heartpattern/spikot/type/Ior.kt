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

import io.heartpattern.spikot.type.Ior.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

public sealed class Ior<out L, out R> {
    public companion object;

    public data class Left<out L>(val left: L) : Ior<L, Nothing>() {
        public companion object
    }

    public data class Right<out R>(val right: R) : Ior<Nothing, R>() {
        public companion object
    }

    public data class Both<out L, out R>(val left: L, val right: R) : Ior<L, R>() {
        public companion object
    }
}

@OptIn(ExperimentalContracts::class)
public fun <L> Ior<L, *>.isLeft(): Boolean {
    contract {
        returns(true) implies (this@isLeft is Left<L>)
    }
    return this is Left<L>
}

@OptIn(ExperimentalContracts::class)
public fun <R> Ior<*, R>.isRight(): Boolean {
    contract {
        returns(true) implies (this@isRight is Right<R>)
    }
    return this is Right<R>
}

@OptIn(ExperimentalContracts::class)
public fun <L, R> Ior<L, R>.isBoth(): Boolean {
    contract {
        returns(true) implies (this@isBoth is Both<L, R>)
    }
    return this is Both<L, R>
}

public inline fun <L, R, T> Ior<L, R>.fold(
    ifLeft: (L) -> T,
    ifRight: (R) -> T,
    ifBoth: (L, R) -> T
): T = when (this) {
    is Left -> ifLeft(left)
    is Right -> ifRight(right)
    is Both -> ifBoth(left, right)
}

public fun <L, R> Ior<L, R>.swap(): Ior<R, L> = fold(
    ifLeft = ::Right,
    ifRight = ::Left,
    ifBoth = { left, right -> Both(right, left) }
)

public inline fun <L, R, T> Ior<L, R>.mapLeft(map: (L) -> T): Ior<T, R> = fold(
    ifLeft = { Left(map(it)) },
    ifRight = { this as Right<R> },
    ifBoth = { left, right -> Both(map(left), right) }
)

public inline fun <L, R, T> Ior<L, R>.mapRight(map: (R) -> T): Ior<L, T> = fold(
    ifLeft = { this as Left<L> },
    ifRight = { Right(map(it)) },
    ifBoth = { left, right -> Both(left, map(right)) }
)

public fun <L> Ior<L, *>.leftToOption(): Option<L> = fold(
    ifLeft = { Some(it) },
    ifRight = { None },
    ifBoth = { left, _ -> Some(left) }
)

public fun <R> Ior<*, R>.rightToOption(): Option<R> = fold(
    ifLeft = { None },
    ifRight = { Some(it) },
    ifBoth = { _, right -> Some(right) }
)

public fun <L> Ior<L, *>.leftToNull(): L? = fold(
    ifLeft = { it },
    ifRight = { null },
    ifBoth = { left, _ -> left }
)

public fun <R> Ior<*, R>.rightToNull(): R? = fold(
    ifLeft = { null },
    ifRight = { it },
    ifBoth = { _, right -> right }
)

public fun <L> Ior<L, *>.leftOrDefault(default: L): L = fold(
    ifLeft = { it },
    ifRight = { default },
    ifBoth = { left, _ -> left }
)

public fun <R> Ior<*, R>.rightOrDefault(default: R): R = fold(
    ifLeft = { default },
    ifRight = { it },
    ifBoth = { _, right -> right }
)

public inline fun <L> Ior<L, *>.leftOrCompute(default: () -> L): L = fold(
    ifLeft = { it },
    ifRight = { default() },
    ifBoth = { left, _ -> left }
)

public inline fun <R> Ior<*, R>.rightOrCompute(default: () -> R): R = fold(
    ifLeft = { default() },
    ifRight = { it },
    ifBoth = { _, right -> right }
)

public fun <T> T.wrapIorRight(): Right<T> = Right(this)
public fun <T> T.wrapIorLeft(): Left<T> = Left(this)
public fun <L, R> Pair<L, R>.wrapIorBoth(): Both<L, R> = Both(first, second)