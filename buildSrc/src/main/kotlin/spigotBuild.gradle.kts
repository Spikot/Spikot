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
    mavenLocal()
    maven("https://maven.heartpattern.io/repository/maven-public/")
}

apply(plugin = "kotlinBuild")

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.1-R0.1-SNAPSHOT")
}

configurations["testRuntime"].extendsFrom(configurations["compileOnly"])

val mainSourceSet = sourceSets["main"]

val mainJarTask = tasks.create<Jar>("mainJar") {
    from(mainSourceSet.output)
}

val mainSourceJarTask = tasks.create<Jar>("mainSourceJar") {
    from(mainSourceSet.allSource)
}

tasks.dokkaHtml {
    outputDirectory = "$buildDir/kdoc"
//    dokkaSourceSets["main"].defaultSetting(project.name)
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            groupId = "io.heartpattern.spikot"
            artifactId = project.name
            this@create.version = project.rootProject.version.toString()

            artifact(mainJarTask)
            artifact(mainSourceJarTask) {
                classifier = "sources"
            }

            pom {
                defaultSetting(project)
                appendDependencies(project, mainSourceSet)
            }
        }
    }
}