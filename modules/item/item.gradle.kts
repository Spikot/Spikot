plugins {
    baseBuild
    spigotBuild
}

dependencies {
    api(modules("nbt"))
    api(modules("adapter"))
}