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
    spigotBuild
    `kotlin-kapt`
}

supportingVersion(
    Version.Spigot.V16,
    Version.Spigot.V15,
    Version.Spigot.V12
)

dependencies {
    api(kotlin("reflect"))
    api("org.reflections", "reflections", Version.reflections)
    implementation("com.google.guava", "guava", Version.guava)

    implementation("org.apache.logging.log4j", "log4j-slf4j-impl", "2.8.1") {
        exclude("org.apache.logging.log4j", "log4j-api")
        exclude("org.apache.logging.log4j", "log4j-core")
    }

    implementation("com.google.auto.service", "auto-service-annotations", Version.auto_service)
    kapt("com.google.auto.service", "auto-service", Version.auto_service)
}