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

package io.heartpattern.spikot.annotation

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.util.*
import kotlin.reflect.jvm.jvmName

internal class MergedAnnotationInvocationHandler(
    private val mergedAnnotation: MergedAnnotation<*>
) : InvocationHandler {
    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
        return when {
            method.isAnnotationAttribute -> mergedAnnotation.getAttribute(method.name)
            method.name == "equals" -> {
                val other = args!![0]
                if (other !is Annotation || !mergedAnnotation.type.isInstance(other))
                    return false

                for (attribute in mergedAnnotation.attributes)
                    if (mergedAnnotation.getAttribute(attribute) != getAttribute(attribute, other))
                        return false

                true
            }
            method.name == "hashCode" -> {
                var hashCode = 0
                for (attribute in mergedAnnotation.attributes) {
                    hashCode *= 31
                    hashCode += mergedAnnotation.getAttribute(attribute).hashCode()
                }

                hashCode
            }
            method.name == "toString" -> {
                val joiner = StringJoiner(", ")
                for (attribute in mergedAnnotation.attributes) {
                    joiner.add("$attribute=${mergedAnnotation.getAttribute(attribute)}")
                }

                "${mergedAnnotation.type.jvmName}($joiner)"
            }
            else -> throw IllegalAccessException("Unhandled method invocation: $method")
        }
    }

    private fun getAttribute(name: String, annotation: Any): Any? {
        return mergedAnnotation.annotationAccessor.getAttributeValue(
            annotation as Annotation,
            mergedAnnotation.annotationAccessor.getAttribute(name)!!
        )
    }
}