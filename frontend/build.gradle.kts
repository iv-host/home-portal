plugins {
    // Resource packaging plugin
    id ("org.ivcode.gradle-www") version "0.1.0-SNAPSHOT"
    // Gradle/Node plugin
    id ("com.github.node-gradle.node") version "7.0.2"
}

// Needed for the www plugin
repositories {
    mavenCentral()
}

// Npm builds to the build directory. The packaging process (this process) will build artifacts into the build-jar directory.
layout.buildDirectory = layout.projectDirectory.dir("build-jar")

// Downloads node and npm for building the frontend
node {
    download = true
    version = "18.16.0"
}

// Creates packages the ui such that it'll be picked up by the backend and served as static resources
www {
    // A unique identifier for the frontend build that'll be picked up by the component scanner in the backend
    packageName = "org.ivcode.homeportal.ui"

    // The directory containing the resources to be packaged
    resources   = "${projectDir}/build"
}

// Task configurations to tie the frontend build to the jar building process
tasks {

    // Configure the clean task to delete the build and node_modules directories
    named("clean").configure {
        doLast {
            File("$projectDir/build").deleteRecursively()
            File("$projectDir/node_modules").deleteRecursively()
        }
    }

    // Configure the build task to depend on the frontend build
    named("www-CopyResources").configure {
        dependsOn("npmInstall", "npm_run_build")
    }
}
