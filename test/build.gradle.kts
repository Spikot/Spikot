plugins {
    kotlin("jvm")
    id("kr.entree.spigradle") version "1.2.2"
}

dependencies {
    implementation(project(":core"))
    implementation("org.spigotmc", "spigot-api", "1.12.2-R0.1-SNAPSHOT")
}

spigot {
    main = "kr.heartpattern.spikot.test.TestPlugin"
}

if (File("local.gradle.kts").exists())
    apply("local.gradle.kts")