plugins {
    baseBuild
    spigotBuild
}

dependencies {
    api(component("core"))
    api(component("adapter"))
}