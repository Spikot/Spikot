plugins {
    baseBuild
    spigotBuild
    kotlin("plugin.serialization")
}

dependencies {
    api("org.jetbrains.kotlinx", "kotlinx-serialization-runtime", Version.serialization)
}