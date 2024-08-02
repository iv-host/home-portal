pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://mvn.ivcode.org/snapshots")
    }
}

rootProject.name = "home-portal"
include("frontend")
include("backend")