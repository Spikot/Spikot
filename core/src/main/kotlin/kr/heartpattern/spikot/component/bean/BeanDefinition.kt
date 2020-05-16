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

package kr.heartpattern.spikot.component.bean

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.component.exception.AnnotationNotFoundException
import kr.heartpattern.spikot.reflection.annotation.MetaAnnotation
import kr.heartpattern.spikot.reflection.annotation.findMetaAnnotations
import kr.heartpattern.spikot.util.Either
import kr.heartpattern.spikot.util.fold
import kr.heartpattern.spikot.util.tryEither
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

/**
 * Represent bean definition of [type] owned by [owingPlugin]
 */
open class BeanDefinition(
    val type: KClass<*>,
    val owingPlugin: SpikotPlugin
) {
    private val annotationCache: MutableMap<String, List<MetaAnnotation<*>>> = mutableMapOf()
    private val attributeCache: MutableMap<AnnotationAttribute, Either<Throwable, Any>> = mutableMapOf()

    /**
     * Scan all meta annotated [T]. This function cache the result.
     */
    inline fun <reified T : Annotation> getMetaAnnotations(): List<MetaAnnotation<T>> {
        return getMetaAnnotations(T::class)
    }

    /**
     * Scan all meta annotated [type]. This function cache the result.
     */
    fun <T : Annotation> getMetaAnnotations(type: KClass<T>): List<MetaAnnotation<T>> {
        @Suppress("UNCHECKED_CAST")
        return annotationCache.getOrPut(type.jvmName) {
            this.type.findMetaAnnotations(type)
        } as List<MetaAnnotation<T>>
    }

    fun getAttribute(annotation: KClass<out Annotation>, attribute: String): Any {
        return attributeCache.getOrPut(AnnotationAttribute(annotation, attribute)) {
            tryEither {
                (getMetaAnnotations(annotation).firstOrNull()
                    ?: throw AnnotationNotFoundException(annotation))
                    .getAttribute(attribute)
            }
        }.fold({ throw it }, { it })
    }

    private data class AnnotationAttribute(val annotation: KClass<*>, val attribute: String)
}

/**
 * Collection of [BeanDefinition]
 */
typealias BeanDefinitionSet = Set<BeanDefinition>