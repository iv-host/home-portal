plugins {
    id ("org.ivcode.gradle-www") version "0.1.0-SNAPSHOT"
    id ("com.github.node-gradle.node") version "7.0.2"
}

repositories {
    mavenCentral()
}

layout.buildDirectory = layout.projectDirectory.dir("build-jar")

node {
    download = true
    version = "18.16.0"
}

www {
    packageName = "org.ivcode.homeportal.ui"
    resources   = "${projectDir}/build"
}

tasks {
    named("clean").configure {
        doLast {
            File("$projectDir/build").deleteRecursively()
            File("$projectDir/node_modules").deleteRecursively()
        }
    }
    named("www-CopyResources").configure {
        dependsOn("npmInstall", "npm_run_build")
    }
}
