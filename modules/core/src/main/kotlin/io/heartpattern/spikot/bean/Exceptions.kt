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

package io.heartpattern.spikot.bean

import kotlin.reflect.KClass

public interface BeanException {
    public val beanDescription: BeanDescription
}

public class InjectException : Exception {
    public val type: KClass<*>
    public val element: String

    public constructor(
        type: KClass<*>,
        element: String
    ) : super() {
        this.type = type
        this.element = element
    }

    public constructor(
        type: KClass<*>,
        element: String,
        message: String
    ) : super(message) {
        this.type = type
        this.element = element
    }

    public constructor(
        type: KClass<*>,
        element: String,
        cause: Throwable
    ) : super(cause) {
        this.type = type
        this.element = element
    }

    public constructor(
        type: KClass<*>,
        element: String,
        message: String,
        cause: Throwable
    ) : super(message, cause) {
        this.type = type
        this.element = element
    }
}

public class BeanCreationException : BeanException, Exception {
    public override val beanDescription: BeanDescription

    public constructor(beanDescription: BeanDescription) : super("Exception thrown while creating bean ${beanDescription.getDeterminedBeanName()}") {
        this.beanDescription = beanDescription
    }

    public constructor(beanDescription: BeanDescription, message: String) : super("Exception thrown while creating bean ${beanDescription.getDeterminedBeanName()}: $message") {
        this.beanDescription = beanDescription
    }

    public constructor(beanDescription: BeanDescription, message: String, cause: Throwable) : super("Exception thrown while creating bean ${beanDescription.getDeterminedBeanName()}: $message", cause) {
        this.beanDescription = beanDescription
    }

    public constructor(beanDescription: BeanDescription, cause: Throwable) : super("Exception thrown while creating bean ${beanDescription.getDeterminedBeanName()}", cause) {
        this.beanDescription = beanDescription
    }
}

public class CircularBeanDependencyException(
    public override val beanDescription: BeanDescription
) : BeanException, Exception("Circular dependency found for ${beanDescription.getDeterminedBeanName()}")