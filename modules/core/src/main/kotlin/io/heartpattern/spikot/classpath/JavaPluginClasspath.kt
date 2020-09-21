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

package io.heartpattern.spikot.classpath

import io.heartpattern.spikot.SpikotPlugin
import io.heartpattern.spikot.reflection.fieldDelegate
import io.heartpattern.spikot.reflection.kotlinGetter
import io.heartpattern.spikot.reflection.kotlinSetter
import io.heartpattern.spikot.util.pluginOf
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ConfigurationBuilder
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.kotlinFunction

internal class JavaPluginClasspath private constructor(
    plugin: JavaPlugin,
    override val parents: Collection<Classpath>,
    basePackage: String? = null
) : Classpath {
    private val JavaPlugin.pluginFile by fieldDelegate<JavaPlugin, File>("file")

    private val reflections = Reflections(
        ConfigurationBuilder()
            .addUrls(plugin.pluginFile.toURI().toURL())
            .addScanners(
                TypeAnnotationsScanner(),
                MethodAnnotationsScanner(),
                AllClassScanner()
            )
            .addClassLoader(plugin::class.java.classLoader)
            .setExpandSuperTypes(false)
            .filterInputsBy { basePackage == null || it.startsWith(basePackage) }
    )
    private val allClass = reflections.scanAllClass().toSet()

    override fun loadClass(name: String): KClass<*>? {
        return if (name !in allClass)
            null
        else
            Class.forName(name).kotlin
    }

    override fun getAllTypes(): Collection<String> {
        return allClass
    }

    override fun getTypesAnnotatedWith(annotation: KClass<out Annotation>): Collection<KClass<*>> {
        return reflections.getTypesAnnotatedWith(annotation.java).map { it.kotlin }
    }

    override fun getFunctionsAnnotatedWith(annotation: KClass<out Annotation>): Collection<KFunction<*>> {
        return reflections.getMethodsAnnotatedWith(annotation.java).map {
            it.kotlinFunction ?: it.kotlinGetter ?: it.kotlinSetter!!
        }
    }

    override fun <T : Any> getSubTypesOf(superType: KClass<T>): Collection<KClass<out T>> {
        return reflections.getSubTypesOf(superType.java).map { it.kotlin }
    }

    companion object {
        private val cache = HashMap<String, JavaPluginClasspath>()

        fun fromPlugin(plugin: JavaPlugin): JavaPluginClasspath {
            return cache.getOrPut(plugin.name) {
                JavaPluginClasspath(
                    plugin,
                    (plugin.description.depend + plugin.description.softDepend).asSequence()
                        .mapNotNull { pluginOf(it) }
                        .filterIsInstance<JavaPlugin>()
                        .map { fromPlugin(it) }
                        .toSet(),
                    (plugin as? SpikotPlugin)?.basePackage
                )
            }
        }
    }
}