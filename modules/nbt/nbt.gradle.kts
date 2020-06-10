plugins {
    baseBuild
    spigotBuild
}

dependencies {
    api(modules("core"))
    api(modules("adapter"))
}