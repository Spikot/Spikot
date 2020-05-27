plugins {
    mavenBuild
}

dependencies {
    rootProject.allprojects {
        if (path.startsWith(":components:")) {
            api(this)
        }
    }
}