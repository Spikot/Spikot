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

package io.heartpattern.spikot.component.classpath

import mu.KotlinLogging
import java.util.*
import kotlin.reflect.KClass

private val logger = KotlinLogging.logger {}

/**
 * Scan collection of classes
 */
interface ClasspathScanner {
    /**
     * All classes scanned by this scanner
     */
    fun getAllTypes(): Collection<String>

    /**
     * Get all class annotated with [annotation]
     */
    fun getTypesAnnotatedWith(annotation: KClass<out Annotation>): Collection<KClass<*>>
}

/**
 * Scan all class scanned by [ClasspathScanner]
 */
object UniversalClasspathScanner : ClasspathScanner {
    private val scanners = LinkedList<ClasspathScanner>()

    fun addScanner(scanner: ClasspathScanner) {
        if (scanner === this)
            throw IllegalArgumentException("Cannot add UniversalBeanRegistry")


        for (registered in scanners) {
            if (scanners === scanner) {
                logger.warn("Attempt to add registered scanner: $scanner")
                return
            }
        }

        scanners.add(scanner)
    }

    fun removeScanner(scanner: ClasspathScanner) {
        val iterator = scanners.iterator()
        while (iterator.hasNext()) {
            if (iterator.next() === scanner) {
                iterator.remove()
                return
            }
        }

        logger.warn("Attempt to remove unregistered scanner: $scanner")
    }

    override fun getAllTypes(): Collection<String> {
        return scanners.flatMap { it.getAllTypes() }
    }

    override fun getTypesAnnotatedWith(annotation: KClass<out Annotation>): Collection<KClass<*>> {
        return scanners.flatMap { it.getTypesAnnotatedWith(annotation) }
    }
}