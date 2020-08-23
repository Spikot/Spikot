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

import io.heartpattern.spikot.type.Either.Left
import io.heartpattern.spikot.type.Either.Right
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

public sealed class Either<out L, out R> {
    public companion object;

    public data class Left<out L>(val left: L) : Either<L, Nothing>() {
        public companion object
    }

    public data class Right<out R>(val right: R) : Either<Nothing, R>() {
        public companion object
    }
}

@OptIn(ExperimentalContracts::class)
public fun <L, R> Either<L, R>.isLeft(): Boolean {
    contract {
        returns(true) implies (this@isLeft is Left<L>)
        returns(false) implies (this@isLeft is Right<R>)
    }
    return this is Left<L>
}

@OptIn(ExperimentalContracts::class)
public fun <L, R> Either<L, R>.isRight(): Boolean {
    contract {
        returns(true) implies (this@isRight is Right<R>)
        returns(false) implies (this@isRight is Left<L>)
    }
    return this is Right<R>
}

public inline fun <L, R, T> Either<L, R>.fold(ifLeft: (L) -> T, ifRight: (R) -> T): T = when (this) {
    is Left -> ifLeft(left)
    is Right -> ifRight(right)
}

public fun <L, R> Either<L, R>.swap(): Either<R, L> = fold(::Right, ::Left)

public inline fun <R, T> Either<*, R>.mapRight(map: (R) -> T): Either<*, T> = when (this) {
    is Left -> this
    is Right -> Right(map(right))
}

public inline fun <L, T> Either<L, *>.mapLeft(map: (L) -> T): Either<T, *> = when (this) {
    is Left -> Left(map(left))
    is Right -> this
}

public fun <R> Either<*, R>.rightToOption(): Option<R> = fold({ None }, { Some(it) })
public fun <L> Either<L, *>.leftToOption(): Option<L> = fold({ Some(it) }, { None })

public fun <R> Either<*, R>.rightOrNull(): R? = fold({ null }, { it })
public fun <L> Either<L, *>.leftOrNull(): L? = fold({ it }, { null })

public fun <R> Either<*, R>.rightOrDefault(default: R): R = fold({ default }, { it })
public fun <L> Either<L, *>.leftOrDefault(default: L): L = fold({ it }, { default })

public inline fun <R> Either<*, R>.rightOrCompute(default: () -> R): R = fold({ default() }, { it })
public inline fun <L> Either<L, *>.leftOrCompute(default: () -> L): L = fold({ it }, { default() })

public fun <T> T.wrapEitherRight(): Right<T> = Right(this)
public fun <T> T.wrapEitherLeft(): Left<T> = Left(this)

public inline fun <T> tryEither(block: () -> T): Either<Exception, T> {
    return try {
        Right(block())
    } catch (e: Exception) {
        Left(e)
    }
}