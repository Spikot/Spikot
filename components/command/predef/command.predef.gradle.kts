plugins {
    baseBuild
    spigotBuild
}

dependencies {
    api(component("command"))
    api(component("chat"))
}