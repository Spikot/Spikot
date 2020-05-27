plugins {
    baseBuild
    spigotBuild
}

dependencies {
    api(component("nbt"))
    api(component("adapter"))
}