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

plugins {
    kotlin("jvm")
}

apply(plugin = "spigotBuild")

afterEvaluate {
    configurations {
        val shade = create("shade")
        shade.extendsFrom(implementation.get())
        shade.extendsFrom(api.get())
        shade.extendsFrom(runtimeOnly.get())
    }

    tasks {
        create<Jar>("createPlugin") {
            dependsOn("jar")
            archiveBaseName.set(project.name + ".plugin")
            from(
                configurations["shade"].map {
                    if (it.isDirectory)
                        it
                    else
                        zipTree(it)
                }
            )
            with(jar.get())
        }
    }
}

