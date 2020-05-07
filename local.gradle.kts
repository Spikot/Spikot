tasks {
    create<Copy>("createPluginAndCopy") {
        dependsOn(get("createPlugin"))
        from("build/libs/")
        include("Spikot-Plugin.jar")
        into(project.properties["pluginPath"] as String)
    }

    create<Exec>("createPluginAndRun") {
        dependsOn(get("createPluginAndCopy"))
        workingDir(project.properties["serverPath"] as String)
        commandLine(
            "java",
            "-Dlog4j.configurationFile=log4j2.xml",
            "-jar",
            project.properties["bukkitPath"] as String
        )
    }
}