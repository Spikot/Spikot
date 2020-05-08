plugins {
    spigotBuild
    pluginBuild
    junitBuild
    kotlin("kapt")
    id("kr.entree.spigradle") version "1.2.2"
}

dependencies {
    api("org.reflections", "reflections", Version.reflections)

    implementation(kotlin("reflect"))

    runtimeOnly("org.slf4j", "slf4j-jdk14", Version.slf4j_bridge)

    compileOnly("com.google.auto.service", "auto-service-annotations", Version.`auto-service`)
    kapt("com.google.auto.service", "auto-service", Version.`auto-service`)
}

spigot {
    main = "kr.heartpattern.spikot.Spikot"
    authors = listOf("HeartPattern")
    website = "https://github.com/Spikot/"
}