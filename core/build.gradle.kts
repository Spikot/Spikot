import kr.entree.spigradle.attribute.Load.STARTUP

plugins {
    spigotBuild
    pluginBuild
    junitBuild
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("kr.entree.spigradle") version "1.2.2"
}

dependencies {
    api("org.reflections", "reflections", Version.reflections)
    api("org.jetbrains.kotlinx", "kotlinx-serialization-runtime", Version.serialization)

    implementation(kotlin("reflect"))

    runtimeOnly("org.slf4j", "slf4j-jdk14", Version.slf4j_bridge)

    compileOnly("com.google.auto.service", "auto-service-annotations", Version.`auto-service`)
    kapt("com.google.auto.service", "auto-service", Version.`auto-service`)
}

spigot {
    name = "Spikot"
    main = "kr.heartpattern.spikot.Spikot"
    version = project.version.toString()
    description = "Spigot plugin framework for Kotlin"
    website = "https://github.com/Spikot/"
    authors = listOf("HeartPattern")
    load = STARTUP
    prefix = "Spikot"
}