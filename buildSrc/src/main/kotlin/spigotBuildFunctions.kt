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

import groovy.util.Node
import groovy.util.NodeList
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*

private val configurationTypes = listOf(
    SourceSet::getAnnotationProcessorConfigurationName,
    SourceSet::getApiConfigurationName,
    SourceSet::getImplementationConfigurationName,
    SourceSet::getCompileClasspathConfigurationName,
    SourceSet::getCompileOnlyConfigurationName,
    SourceSet::getRuntimeClasspathConfigurationName,
    SourceSet::getRuntimeOnlyConfigurationName
)

/**
 * Extends all dependencies from [from]
 */
private fun Project.extendsDependencies(from: SourceSet, to: SourceSet) {
    for (configurationType in configurationTypes) {
        val fromConfiguration = configurations[configurationType(from)]
        val toConfiguration = configurations[configurationType(to)]
        toConfiguration.extendsFrom(fromConfiguration)
    }
}

fun Project.supportingVersion(vararg versions: Version.Spigot) {
    val sourceSets = the<SourceSetContainer>()
    val mainSourceSet = sourceSets["main"]

    // Setup source sets
    for (version in versions) {
        // Lowercase version name
        val versionName = version.name.toLowerCase()

        // Version specific source set
        val versionSourceSet = sourceSets.create(versionName) {
            java.setSrcDirs(listOf("src/$versionName/kotlin"))
            resources.setSrcDirs(listOf("src/$versionName/resources"))
            compileClasspath += mainSourceSet.output
        }

        dependencies.add(versionSourceSet.implementationConfigurationName, mainSourceSet.output)
        dependencies.add(versionSourceSet.implementationConfigurationName, mainSourceSet.compileClasspath)
        dependencies.add("${versionName}CompileOnly", "org.spigotmc:spigot-api:${version.fullVersion}")
        dependencies.add("${versionName}CompileOnly", "org.spigotmc:spigot:${version.fullVersion}")
//        extendsDependencies(mainSourceSet, versionSourceSet)
    }


    // Setup maven publication
    for (version in versions) {
        val versionName = version.name.toLowerCase()
        val versionSourceSet = sourceSets.getByName(versionName)

        val versionJarTask = tasks.create<Jar>("${versionName}Jar") {
            from(versionSourceSet.output)
            archiveAppendix.set(versionName)
        }

        val versionSourceJarTask = tasks.create<Jar>("${versionName}SourceJar") {
            from(versionSourceSet.allSource)
            archiveAppendix.set(versionName)
        }

        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("${project.name}${version.name}") {
                    groupId = "io.heartpattern.spikot"
                    artifactId = project.name + "." + version.name.toLowerCase()
                    this@create.version = project.rootProject.version.toString()

                    artifact(versionJarTask)
                    artifact(versionSourceJarTask) {
                        classifier = "sources"
                    }

                    pom {
                        defaultSetting(this@supportingVersion)
                        appendDependencies(this@supportingVersion, versionSourceSet)
                        withXml {
                            ((asNode().get("dependencies") as NodeList)[0] as Node).appendNode("dependency").apply {
                                appendNode("groupId", "io.heartpattern.spikot")
                                appendNode("artifactId", project.name)
                                appendNode("version", project.rootProject.version.toString())
                                appendNode("scope", "compile")
                                appendNode("exclusions").apply {
                                    appendNode("exclusion").apply {
                                        appendNode("groupId", "org.spigotmc")
                                        appendNode("artifactId", "spigot-api")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    tasks {
        create("compileAll") {
            dependsOn(
                *versions.map { "compile${it.name}Kotlin" }.toTypedArray()
            )
        }

        getByName<Jar>("jar"){
            dependsOn(
                *versions.map{"compile${it.name}Kotlin"}.toTypedArray()
            )
            from(
                *versions.map{
                    sourceSets.getByName(it.name.toLowerCase()).output
                }.toTypedArray()
            )
        }
    }
}

fun DependencyHandler.versionApi(version: Version.Spigot, notation: Any): Dependency? {
    return add("${version.name.toLowerCase()}Api", notation)
}

fun DependencyHandler.versionImplementation(version: Version.Spigot, notation: Any): Dependency? {
    return add("${version.name.toLowerCase()}Implementation", notation)
}

fun DependencyHandler.versionRuntimeOnly(version: Version.Spigot, notation: Any): Dependency? {
    return add("${version.name.toLowerCase()}RuntimeOnly", notation)
}

fun DependencyHandler.versionCompileOnly(version: Version.Spigot, notation: Any): Dependency? {
    return add("${version.name.toLowerCase()}CompileOnly", notation)
}