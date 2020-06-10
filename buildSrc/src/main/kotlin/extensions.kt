import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

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

@Suppress("unused")
fun DependencyHandler.kotlinx(module: String, version: String? = null): Any =
    "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$version" } ?: ""}"

@Suppress("unused")
fun DependencyHandler.spigot(version: String): Any =
    "org.spigotmc:spigot:$version"

fun Project.modules(module: String): Project = project(":modules:$module")
fun Project.examples(module: String): Project = project(":examples:$module")
fun Project.packages(module: String): Project = project(":packages:$module")
fun Project.adapters(module: String): Project = project(":adapters:$module")