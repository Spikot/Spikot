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

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
//    id("org.jetbrains.dokka")
    `maven-publish`
}

repositories {
    maven("https://maven.heartpattern.io/repository/maven-public/")
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    api("org.jetbrains.kotlinx", "kotlinx-coroutines-core", Version.coroutine)
    api("org.jetbrains.kotlinx","kotlinx-coroutines-jdk8", Version.coroutine)
    api("org.jetbrains.kotlinx", "kotlinx-serialization-core", Version.serialization)
    api("io.github.microutils", "kotlin-logging", Version.kotlin_logging)

    testImplementation("org.junit.jupiter", "junit-jupiter", Version.junit_jupyter)
    testImplementation("io.mockk", "mockk", Version.mockk)
    testImplementation("org.slf4j", "slf4j-api", Version.slf4j)
    testRuntimeOnly("org.apache.logging.log4j", "log4j-core", Version.log4j2)
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn" + "-progressive"
            allWarningsAsErrors = true
        }
    }

    test {
        useJUnitPlatform()
    }
}

kotlin {
    explicitApi()
}

publishing {
    repositories {
        maven(
            if (rootProject.version.toString().endsWith("SNAPSHOT"))
                Constant.SNAPSHOT_REPOSITORY
            else
                Constant.RELEASE_REPOSITORY
        ) {
            credentials {
                username = project.properties["maven.username"].toString()
                password = project.properties["maven.password"].toString()
            }

            name = if (rootProject.version.toString().endsWith("SNAPSHOT"))
                "Snapshot"
            else
                "Releases"
        }
    }
}