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

version = "5.1.1-SNAPSHOT"

allprojects {
    group = "io.heartpattern.spikot"
    version = "5.1.1-SNAPSHOT"
}

repositories {
    maven("https://maven.heartpattern.io/repository/maven-public/")
}

evaluationDependsOnChildren()

val shade = configurations.create("shade")

dependencies {
    subprojects {
        if (path.startsWith(":modules:"))
            shade(this@subprojects)
    }
}

tasks {
    create("generatePluginYml") {
        doLast {
            buildDir.mkdirs()
            val file = File("$buildDir/plugin.yml")
            file.createNewFile()
            val authors = File("$rootDir/AUTHORS.md").readLines()
                .asSequence()
                .filter { it.startsWith("- ") }
                .map { it.substring(2) }
                .joinToString(", ")

            file.writeText("""
            name: Spikot
            main: io.heartpattern.spikot.Spikot
            version: ${project.version}
            description: Spigot plugin framework for Kotlin
            website: https://github.com/Spikot/
            authors: [$authors]
            provides: [kotlin-stdlib-jdk8, kotlin]
            load: STARTUP
        """.trimIndent())
        }
    }

    create<Jar>("build") {
        dependsOn("generatePluginYml")
        dependsOn(shade)
        from(shade.map { if (it.isDirectory) it else zipTree(it) })
        from(File("$buildDir/plugin.yml"))
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        destinationDirectory.set(File("$buildDir/libs"))
        archiveFileName.set("Spikot.plugin-${project.version}.jar")
    }

    create("clean"){
        File(project.rootDir, "build").deleteRecursively()
    }
}