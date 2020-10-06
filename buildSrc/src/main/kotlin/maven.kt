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
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.withType
import java.io.File

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

/**
 * Apply default setting of pom.xml
 */
fun MavenPom.defaultSetting(project: Project) {
    name.set("spikot ${project.name}")
    description.set("Spikot framework ${project.name} submodule")
    url.set("https://github.com/Spikot/Spikot")
    packaging = "jar"
    licenses {
        license {
            name.set("MIT License")
            url.set("http://www.opensource.org/licenses/mit-license")
            distribution.set("repo")
        }
    }
    developers {
        for (developer in project.parseDeveloper()) {
            developer {
                id.set(developer.name)
                name.set(developer.name)
                email.set(developer.email)
                url.set(developer.url)
                timezone.set(developer.timezone)
            }
        }
    }
    scm {
        connection.set("scm:git:https://github.com/Spikot/Spikot.git")
        developerConnection.set("scm:git:https://github.com/Spikot/Spikot.git")
        tag.set("HEAD")
        url.set("https://github.com/Spikot/Spikot")
    }
    issueManagement {
        system.set("github")
        url.set("https://github.com/Spikot/Spikot/issues")
    }
    ciManagement {
        system.set("TeamCity")
        url.set("https://teamcity.heartpattern.io/")
    }
}

/**
 * Append [sourceSet]'s dependencies
 */
fun MavenPom.appendDependencies(project: Project, sourceSet: SourceSet) {
    withXml {
        val dependencies = asNode().appendNode("dependencies")
        for ((configurationNameGetter, scope) in configurationMavenMapping) {
            val configurationName = configurationNameGetter(sourceSet)
            for (dependency in project.configurations[configurationName].dependencies.withType<ModuleDependency>()) {
                if (dependency.group == "org.spigotmc" && dependency.name == "spigot")
                    continue

                dependencies.appendDependency(dependency, scope)
            }
        }
    }
}

fun Node.appendDependency(dependency: Dependency, scope: String) {
    val dependencyNode = appendNode("dependency")
    dependencyNode.appendNode("groupId", dependency.group)
    dependencyNode.appendNode("artifactId", dependency.name)
    dependencyNode.appendNode("version", dependency.version)
    dependencyNode.appendNode("scope", scope)
}

private val configurationMavenMapping = mapOf(
    SourceSet::getApiConfigurationName to "compile",
    SourceSet::getCompileOnlyConfigurationName to "compile",
    SourceSet::getRuntimeOnlyConfigurationName to "runtime"
)

private data class Developer(
    val name: String,
    val email: String,
    val url: String,
    val timezone: String
)

private fun Project.parseDeveloper(): List<Developer> {
    return File(project.rootDir, "AUTHORS.md").readLines().chunked(6).map { line ->
        val name = line[0].substringAfter("- ")
        val email = line[1].substringAfter("Email: ")
        val website = line[3].substringAfter("Website: ")
        val timezone = line[4].substringAfter("Time zone: ")
        Developer(name, email, website, timezone)
    }
}