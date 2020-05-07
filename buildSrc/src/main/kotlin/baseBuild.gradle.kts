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
import java.net.URL

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    `maven-publish`
}

repositories {
    maven("https://maven.heartpattern.kr/repository/maven-public/")
}

dependencies {
    api(kotlin("stdlib"))
    api("org.jetbrains.kotlinx", "kotlinx-coroutines-core", Version.coroutine)
    api("io.github.microutils", "kotlin-logging", Version.kotlin_logging)
}

tasks {
    test {
        useJUnitPlatform()
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }

    create<Jar>("dokkaJar") {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        archiveClassifier.set("javadoc")
        from(dokka)
    }

    create<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    dokka {
        outputFormat = "html"
        outputDirectory = "$buildDir/kdoc/"
        configuration {
            externalDocumentationLink {
                url = URL("https://hub.spigotmc.org/javadocs/spigot/")
                packageListUrl = URL("https://hub.spigotmc.org/javadocs/spigot/package-list")
            }
        }
    }
}

if ("maven.user" in properties && "maven.password" in properties) {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = name
                from(components["java"])
                artifact(tasks["dokkaJar"])
                artifact(tasks["sourcesJar"])
            }
        }
        repositories {
            maven(
                if (version.toString().endsWith("SNAPSHOT"))
                    "https://maven.heartpattern.kr/repository/maven-public-snapshots/"
                else
                    "https://maven.heartpattern.kr/repository/maven-public-releases/"
            ) {
                credentials {
                    username = properties["maven.user"] as String
                    password = properties["maven.password"] as String
                }
            }
        }
    }
}
