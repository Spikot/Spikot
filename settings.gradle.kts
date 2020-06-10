rootProject.name = "Spikot"

val modules = listOf(
    "core",
    "command",
    "command:predef",
    "chat",
    "nbt",
    "item",
    "reflection",
    "annotation",
    "service",
    "event",
    "serialization",
    "type",
    "extension",
    "util",
    "adapter"
)

val adapters = listOf(
    "1.12"
)

val packages = listOf(
    "all"
)

val examples = listOf(
    "test"
)

val allProjects = mapOf(
    "modules" to modules,
    "adapters" to adapters,
    "packages" to packages,
    "examples" to examples
)

for ((directory, projects) in allProjects) {
    for (project in projects) {
        include("$directory:$project")
        findProject(":$directory:$project")!!.buildFileName = "${project.replace(':', '.')}.gradle.kts"
    }
}
