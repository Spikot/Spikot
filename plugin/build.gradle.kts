plugins {
    base
}

repositories {
    maven("https://maven.heartpattern.kr/repository/maven-public/")
}

val shade = configurations.create("shade")

dependencies {
    shade(project(":core"))
    shade(project(":adapters:1.12"))
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
                main: kr.heartpattern.spikot.Spikot
                version: ${project.version}
                description: Spigot plugin framework for Kotlin
                website: https://github.com/Spikot/
                authors: [$authors]
                load: POSTWORLD
            """.trimIndent())
        }
    }

    create<Jar>("package") {
        dependsOn("generatePluginYml")
        dependsOn(shade)
        from(shade.map { zipTree(it) })
        from(File("$buildDir/plugin.yml"))
        destinationDirectory.set(File("$buildDir/libs"))
        archiveFileName.set("Spikot.plugin-${project.version}.jar")
    }
}