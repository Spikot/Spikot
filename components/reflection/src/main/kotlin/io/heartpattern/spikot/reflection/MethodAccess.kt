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

@file:Suppress("UNCHECKED_CAST")

package io.heartpattern.spikot.reflection

import java.lang.reflect.Method

/**
 * Delegate property to access method of [R] which return [T]
 */
class MethodAccess0<R : Any, T> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method
     * @return Result of method
     */
    operator fun invoke(): T {
        return method.invoke(thisRef) as T
    }
}

/**
 * Delegate property to access method of [R] which return [T] and take [P1] as parameter
 */
class MethodAccess1<R : Any, T, P1> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1]
     */
    operator fun invoke(p1: P1): T {
        return method.invoke(thisRef, p1) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2] as parameter
 */
class MethodAccess2<R : Any, T, P1, P2> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1], [p2]
     */
    operator fun invoke(p1: P1, p2: P2): T {
        return method.invoke(thisRef, p1, p2) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2], [P3] as parameter
 */
class MethodAccess3<R : Any, T, P1, P2, P3> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }


    /**
     * Invoke method with parameter [p1], [p2], [p3]
     */
    operator fun invoke(p1: P1, p2: P2, p3: P3): T {
        return method.invoke(thisRef, p1, p2, p3) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2], [P3], [P4] as parameter
 */
class MethodAccess4<R : Any, T, P1, P2, P3, P4> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1], [p2], [p3], [p4]
     */
    operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4): T {
        return method.invoke(thisRef, p1, p2, p3, p4) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2], [P3], [P4], [P5] as parameter
 */
class MethodAccess5<R : Any, T, P1, P2, P3, P4, P5> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1], [p2], [p3], [p4], [p5]
     */
    operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): T {
        return method.invoke(thisRef, p1, p2, p3, p4, p5) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2], [P3], [P4], [P5], [P6] as parameter
 */
class MethodAccess6<R : Any, T, P1, P2, P3, P4, P5, P6> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1], [p2], [p3], [p4], [p5], [p6]
     */
    operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6): T {
        return method.invoke(thisRef, p1, p2, p3, p4, p5, p6) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2], [P3], [P4], [P5], [P6], [P7] as parameter
 */
class MethodAccess7<R : Any, T, P1, P2, P3, P4, P5, P6, P7> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1], [p2], [p3], [p4], [p5], [p6], [p7]
     */
    operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7): T {
        return method.invoke(thisRef, p1, p2, p3, p4, p5, p6, p7) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2], [P3], [P4], [P5], [P6], [P7], [P8] as parameter
 */
class MethodAccess8<R : Any, T, P1, P2, P3, P4, P5, P6, P7, P8> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1], [p2], [p3], [p4], [p5], [p6], [p7], [p8]
     */
    operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8): T {
        return method.invoke(thisRef, p1, p2, p3, p4, p5, p6, p7, p8) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2], [P3], [P4], [P5], [P6], [P7], [P8], [P9] as parameter
 */
class MethodAccess9<R : Any, T, P1, P2, P3, P4, P5, P6, P7, P8, P9> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1], [p2], [p3], [p4], [p5], [p6], [p7], [p8], [p9]
     */
    operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9): T {
        return method.invoke(thisRef, p1, p2, p3, p4, p5, p6, p7, p8, p9) as T
    }
}