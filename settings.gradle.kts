/*
 * Copyright (c) 2020 HeartPattern and Spikot authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

rootProject.name = "Spikot"

val modules = listOf<String>(
    "core",
    "packet",
    "advancement",
    "chat",
    "nbt",
    "item",
    "adapter",
    "disposable-command",
    "menu"
)

val examples = listOf<String>(
    "test"
)

val tests = listOf<String>(
//    "engine"
)

val allProjects = mapOf(
    "modules" to modules,
    "examples" to examples,
    "tests" to tests
)

for ((directory, projects) in allProjects) {
    for (project in projects) {
        include("$directory:$project")
        findProject(":$directory:$project")!!.buildFileName = "${project.replace(':', '.')}.gradle.kts"
    }
}