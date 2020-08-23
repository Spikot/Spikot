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

/**
 * Mark class as bean.
 */
@Target(
    AnnotationTarget.CLASS
)
public annotation class Component

/**
 * Mark function as bean
 */
@Target(
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Bean(
    /**
     * Init function of bean. Method will invoke after bean is initialized
     */
    val initialize: String = "",
    /**
     * Destroy function of bean. Method will invoke after bean is destroyed
     */
    val destroy: String = ""
)

/**
 * Specify scope of bean. By default, scope is singleton.
 */
@Target(
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Scope(
    val name: String = "singleton"
)

/**
 * Name a marked bean to [name]
 */
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Qualifier(
    /**
     * Name of marked bean
     */
    val name: String = ""
)

/**
 * Mark this bean is primary bean. If multiple bean is defined for same class, primary bean is selected.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
public annotation class Primary

/**
 * Mark this element should be injected.
 */
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Inject

/**
 * Mark explicit dependency of this bean
 */
@Target(
    AnnotationTarget.CLASS
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class DependsOn(
    vararg val dependencies: String
)

/**
 * Mark this method should invoked after bean constructed and depending beans are injected
 */
@Target(
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class AfterInitialize

/**
 * Mark this method should invoked before bean destroy and depending beans are removed
 */
@Target(
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class BeforeDestroy

/**
 * Set load order of marked bean. If inject or explicit dependencies conflict with load order, load order will be ignored.
 * Higher value load later.
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.CLASS
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class LoadOrder(
    val order: Int = NORMAL
) {
    public companion object {
        /**
         * Internally used priority.
         */
        internal const val SYSTEM_FASTEST: Int = Int.MIN_VALUE

        /**
         * Fastest priority. Bean with this priority will load firstly and unload fifthly.
         */
        public const val FASTEST: Int = Int.MIN_VALUE / 2

        /**
         * Faster priority. Bean with this priority will load secondly and unload fourthly.
         */
        public const val FAST: Int = -100000

        /**
         * Normal priority. Bean with this priority will load thirdly and unload thirdly.
         * Bean that does not specify load order has this priority.
         */
        public const val NORMAL: Int = 0

        /**
         * Late priority. Bean with this priority will load fourthly and unload secondly.
         */
        public const val LATE: Int = 100000

        /**
         * Latest priority. Bean with this priority will load fifthly and unload firstly.
         */
        public const val LATEST: Int = Int.MAX_VALUE / 2

        /**
         * Internally used priority.
         */
        internal const val SYSTEM_LATEST: Int = Int.MIN_VALUE
    }
}

/**
 * Mark this bean is lazily loaded. Bean will not create if bean has no usage.
 * If bean is used somewhere, this annotation has no effect.
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.CLASS
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Lazy

/**
 * Mark this bean should initialize in load phase of bukkit plugin lifecycle. This annotation has no effect for scope
 * other than singleton.
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.CLASS
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class EarlyLoad