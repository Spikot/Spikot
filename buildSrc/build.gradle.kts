plugins {
    `kotlin-dsl`
}

repositories {
    maven("https://maven.heartpattern.kr/repository/maven-public/")
}

dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.3.71")
    implementation("org.jetbrains.kotlin", "kotlin-serialization", "1.3.71")
    implementation("org.jetbrains.dokka", "dokka-gradle-plugin", "0.10.1")
}