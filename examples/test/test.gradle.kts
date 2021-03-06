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

plugins {
    kotlin("jvm")
    id("kr.entree.spigradle") version "2.1.1"
}

repositories{
    mavenLocal()
    maven("https://maven.heartpattern.io/repository/maven-public/")
}

dependencies {
    implementation(modules("core"))
    implementation(modules("packet"))
    implementation(modules("advancement"))
    implementation(modules("menu"))
    implementation("org.spigotmc", "spigot-api", "1.16.4-R0.1-SNAPSHOT")
    implementation("org.spigotmc", "spigot", "1.16.4-R0.1-SNAPSHOT")
}

spigot {
    main = "io.heartpattern.spikot.test.TestPlugin"
    depends = listOf("Spikot")
    apiVersion = "1.16"

    commands{
        create("test_achievement")
        create("open_menu")
    }
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