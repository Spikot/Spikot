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

package io.heartpattern.spikot.type

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class Either<out L, out R> {
    companion object
}

data class Left<out L>(val left: L) : Either<L, Nothing>() {
    companion object
}

data class Right<out R>(val right: R) : Either<Nothing, R>() {
    companion object
}

@OptIn(ExperimentalContracts::class)
fun <L, R> Either<L, R>.isLeft(): Boolean {
    contract {
        returns(true) implies (this@isLeft is Left<L>)
        returns(false) implies (this@isLeft is Right<R>)
    }
    return this is Left<L>
}

@OptIn(ExperimentalContracts::class)
fun <L, R> Either<L, R>.isRight(): Boolean {
    contract {
        returns(true) implies (this@isRight is Right<R>)
        returns(false) implies (this@isRight is Left<L>)
    }
    return this is Right<R>
}

inline fun <L, R, T> Either<L, R>.fold(ifLeft: (L) -> T, ifRight: (R) -> T): T = when (this) {
    is Left -> ifLeft(left)
    is Right -> ifRight(right)
}

fun <L, R> Either<L, R>.swap(): Either<R, L> = fold({ Right(it) }, { Left(it) })

inline fun <R, T> Either<*, R>.map(map: (R) -> T): Either<*, T> = when (this) {
    is Left -> this
    is Right -> Right(map(right))
}

inline fun <L, T> Either<L, *>.mapLeft(map: (L) -> T): Either<T, *> = when (this) {
    is Left -> Left(map(left))
    is Right -> this
}

fun <R> Either<*, R>.toOption(): Option<R> = fold({ None }, { Some(it) })
fun <L> Either<L, *>.toLeftOption(): Option<L> = fold({ Some(it) }, { None })

fun <R> Either<*, R>.orNull(): R? = fold({ null }, { it })
fun <L> Either<L, *>.orLeftNull(): L? = fold({ it }, { null })

fun <R> Either<*, R>.orDefault(default: R): R = fold({ default }, { it })
fun <L> Either<L, *>.orLeftDefault(default: L): L = fold({ it }, { default })

inline fun <R> Either<*, R>.orCompute(default: () -> R) = fold({ default() }, { it })
inline fun <L> Either<L, *>.orLeftCompute(default: () -> L) = fold({ it }, { default() })

fun <T> T.right() = Right(this)
fun <T> T.left() = Left(this)

inline fun <T> tryEither(block: () -> T): Either<Exception, T> {
    return try {
        Right(block())
    } catch (e: Exception) {
        Left(e)
    }
}