pluginManagement {
    repositories {
        maven { url = uri("https://s3.us-west-2.amazonaws.com/maven.ivcode.org/snapshot/") }
        maven { url = uri("https://s3.us-west-2.amazonaws.com/maven.ivcode.org/release/") }
        mavenCentral()
        gradlePluginPortal()
    }

}

rootProject.name = "home-portal"
include("frontend")
include("backend")