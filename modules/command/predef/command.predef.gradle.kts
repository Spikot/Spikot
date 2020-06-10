plugins {
    baseBuild
    spigotBuild
}

dependencies {
    api(modules("command"))
    api(modules("chat"))
}