plugins {
    baseBuild
    junitBuild
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(modules("reflection"))
}