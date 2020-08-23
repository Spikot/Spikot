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

@file:Suppress("UNCHECKED_CAST")

package io.heartpattern.spikot.reflection

import java.lang.reflect.Method

/**
 * Delegate property to access method of [R] which return [T]
 */
public class MethodAccess0<R : Any, T> @PublishedApi internal constructor(
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
    public operator fun invoke(): T {
        return method.invoke(thisRef) as T
    }
}

/**
 * Delegate property to access method of [R] which return [T] and take [P1] as parameter
 */
public class MethodAccess1<R : Any, T, P1> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1]
     */
    public operator fun invoke(p1: P1): T {
        return method.invoke(thisRef, p1) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2] as parameter
 */
public class MethodAccess2<R : Any, T, P1, P2> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1], [p2]
     */
    public operator fun invoke(p1: P1, p2: P2): T {
        return method.invoke(thisRef, p1, p2) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2], [P3] as parameter
 */
public class MethodAccess3<R : Any, T, P1, P2, P3> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }


    /**
     * Invoke method with parameter [p1], [p2], [p3]
     */
    public operator fun invoke(p1: P1, p2: P2, p3: P3): T {
        return method.invoke(thisRef, p1, p2, p3) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2], [P3], [P4] as parameter
 */
public class MethodAccess4<R : Any, T, P1, P2, P3, P4> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1], [p2], [p3], [p4]
     */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4): T {
        return method.invoke(thisRef, p1, p2, p3, p4) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2], [P3], [P4], [P5] as parameter
 */
public class MethodAccess5<R : Any, T, P1, P2, P3, P4, P5> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1], [p2], [p3], [p4], [p5]
     */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): T {
        return method.invoke(thisRef, p1, p2, p3, p4, p5) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2], [P3], [P4], [P5], [P6] as parameter
 */
public class MethodAccess6<R : Any, T, P1, P2, P3, P4, P5, P6> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1], [p2], [p3], [p4], [p5], [p6]
     */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6): T {
        return method.invoke(thisRef, p1, p2, p3, p4, p5, p6) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2], [P3], [P4], [P5], [P6], [P7] as parameter
 */
public class MethodAccess7<R : Any, T, P1, P2, P3, P4, P5, P6, P7> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1], [p2], [p3], [p4], [p5], [p6], [p7]
     */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7): T {
        return method.invoke(thisRef, p1, p2, p3, p4, p5, p6, p7) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2], [P3], [P4], [P5], [P6], [P7], [P8] as parameter
 */
public class MethodAccess8<R : Any, T, P1, P2, P3, P4, P5, P6, P7, P8> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1], [p2], [p3], [p4], [p5], [p6], [p7], [p8]
     */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8): T {
        return method.invoke(thisRef, p1, p2, p3, p4, p5, p6, p7, p8) as T
    }
}


/**
 * Delegate property to access method of [R] which return [T] and take [P1], [P2], [P3], [P4], [P5], [P6], [P7], [P8], [P9] as parameter
 */
public class MethodAccess9<R : Any, T, P1, P2, P3, P4, P5, P6, P7, P8, P9> @PublishedApi internal constructor(
    private val thisRef: R,
    private val method: Method
) {
    init {
        method.isAccessible = true
    }

    /**
     * Invoke method with parameter [p1], [p2], [p3], [p4], [p5], [p6], [p7], [p8], [p9]
     */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9): T {
        return method.invoke(thisRef, p1, p2, p3, p4, p5, p6, p7, p8, p9) as T
    }
}