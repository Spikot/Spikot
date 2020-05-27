plugins {
    baseBuild
    spigotBuild
}

dependencies {
    api(component("core"))
    api(component("chat"))
    api(component("adapter"))
    implementation(kotlin("reflect"))
}