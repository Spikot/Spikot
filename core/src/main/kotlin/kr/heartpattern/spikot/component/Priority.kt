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

package kr.heartpattern.spikot.component

/**
 * Specify enable order of bean. Smaller [priority] enable earlier and disable later.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Priority(val priority: Int) {
    companion object {
        /**
         * Lowest level of priority. Enable first, disable last.
         */
        const val EARLIEST = -20000

        /**
         * Lower level of priority
         */
        const val EARLY = -10000

        /**
         * Default priority. Bean which does not have [Priority] annotation has this priority.
         */
        const val DEFAULT = 0

        /**
         * Higher level of priority
         */
        const val LATE = 10000

        /**
         * Highest level of priority. Enable last, disable first.
         */
        const val LATEST = 20000
    }
}