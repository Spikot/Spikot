plugins {
    baseBuild
    junitBuild
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(component("reflection"))
}