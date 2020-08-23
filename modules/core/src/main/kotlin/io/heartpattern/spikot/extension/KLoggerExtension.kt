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

package io.heartpattern.spikot.extension

import mu.KLogger
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Catch all exception of [block] and log stacktrace with [message].
 * Return result of [block] typed [T] or null if exception thrown.
 */
@OptIn(ExperimentalContracts::class)
public inline fun <T> KLogger.catchAll(message: String, block: () -> T): T? {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }

    return try {
        block()
    } catch (e: Exception) {
        error(e) { message }
        null
    }
}

/**
 * Catch exception [E] of [block] and log stacktrace with [message].
 * Return result of [block] typed [T] or null if exception thrown.
 */
@OptIn(ExperimentalContracts::class)
public inline fun <reified E : Throwable, T> KLogger.catch(message: String, block: () -> T): T? {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }

    return try {
        block()
    } catch (e: Exception) {
        if (e is E) {
            error(e) { message }
            null
        } else {
            throw e
        }
    }
}

public fun KLogger.check(condition: Boolean, message: () -> Any) {
    if (condition)
        error(message)
}