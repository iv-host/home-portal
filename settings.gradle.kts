pluginManagement {
    repositories {
        maven { url = uri("https://mvn.ivcode.org/mvn/snapshot") }
        mavenCentral()
        gradlePluginPortal()
    }

}

rootProject.name = "home-portal"
include("frontend")
include("backend")