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

package kr.heartpattern.spikot.component.classpath

import org.bukkit.Bukkit
import org.reflections.Reflections
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import kotlin.reflect.KClass

/**
 * [ClasspathScanner] implementation using [Reflections]
 */
class ReflectionsClasspathScanner(
    private val reflections: Reflections
) : ClasspathScanner {

    /**
     * Scan classes managed by given [classLoader]
     */
    constructor(classLoader: ClassLoader) : this(
        ConfigurationBuilder()
            .addUrls(ClasspathHelper.forClassLoader(classLoader))
            .addScanners(
                TypeAnnotationsScanner(),
                AllClassScanner()
            )
            .addClassLoaders(
                classLoader,
                Bukkit::class.java.classLoader
            )
            .run {
                Reflections(this)
            }
    )

    override fun getAllTypes(): Collection<String> {
        return reflections.scanAllClass()
    }

    override fun getTypesAnnotatedWith(annotation: KClass<out Annotation>): Collection<KClass<*>> {
        return reflections.getTypesAnnotatedWith(annotation.java).map { it.kotlin }
    }
}