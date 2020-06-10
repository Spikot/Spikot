plugins {
    kotlin("jvm")
    id("kr.entree.spigradle") version "1.2.2"
}

dependencies {
    implementation(modules("core"))
    implementation(modules("command"))
    implementation(modules("command:predef"))
    implementation("org.spigotmc", "spigot-api", "1.12.2-R0.1-SNAPSHOT")
}

spigot {
    main = "io.heartpattern.spikot.test.TestPlugin"
    depends = listOf("Spikot")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

if (File(projectDir, "local.gradle.kts").exists())
    apply("local.gradle.kts")