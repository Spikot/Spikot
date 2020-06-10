plugins {
    mavenBuild
}

dependencies {
    rootProject.allprojects {
        if (path.startsWith(":modules:")) {
            api(this)
        }
    }
}