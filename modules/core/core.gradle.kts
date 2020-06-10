plugins {
    spigotBuild
    pluginBuild
    junitBuild
    kotlin("kapt")
}

dependencies {
    api("org.reflections", "reflections", Version.reflections)
    api(modules("annotation"))
    api(modules("type"))
    api(modules("util"))
    api(modules("extension"))

    implementation(kotlin("reflect"))
    implementation(modules("reflection"))

    runtimeOnly("org.slf4j", "slf4j-jdk14", Version.slf4j_bridge)

    compileOnly("com.google.auto.service", "auto-service-annotations", Version.`auto-service`)
    kapt("com.google.auto.service", "auto-service", Version.`auto-service`)
}