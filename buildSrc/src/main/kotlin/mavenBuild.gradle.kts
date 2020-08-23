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
    id("org.jetbrains.dokka")
    `maven-publish`
}

repositories {
    maven("https://maven.heartpattern.io/repository/maven-public/")
}

tasks {
    create<Jar>("dokkaJar") {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        archiveClassifier.set("javadoc")
        from(dokkaHtml)
    }

    create<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    dokkaHtml {
        outputDirectory = "$buildDir/kdoc/"
        dokkaSourceSets["main"].defaultSetting(project.name)
    }
}

if ("maven.user" in properties && "maven.password" in properties) {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                val path = project.path
                artifactId = when {
                    path.startsWith(":modules") -> path.substring(8)
                    path.startsWith(":packages") -> path.substring(9)
                    else -> path
                }.replace(':', '-').substring(1)
                from(components["java"])
                artifact(tasks["dokkaJar"])
                artifact(tasks["sourcesJar"])
            }
        }
        repositories {
            maven(
                if (version.toString().endsWith("SNAPSHOT"))
                    "https://maven.heartpattern.io/repository/spikot-snapshots/"
                else
                    "https://maven.heartpattern.io/repository/spikot-releases/"
            ) {
                credentials {
                    username = properties["maven.user"] as String
                    password = properties["maven.password"] as String
                }
            }
        }
    }
}
