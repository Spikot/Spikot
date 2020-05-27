allprojects {
    group = "io.heartpattern.spikot"
    version = "0.1.0-SNAPSHOT"
}

repositories {
    maven("https://maven.heartpattern.io/repository/maven-public/")
}

evaluationDependsOnChildren()

val shade = configurations.create("shade")

dependencies {
    subprojects {
        if (path.startsWith(":components:") || path.startsWith(":adapters:"))
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
}
