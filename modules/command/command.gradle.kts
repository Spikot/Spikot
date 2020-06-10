plugins {
    baseBuild
    spigotBuild
}

dependencies {
    api(modules("core"))
    api(modules("chat"))
    api(modules("adapter"))
    api(modules("type"))
    implementation(kotlin("reflect"))
}