plugins {
    id ("com.github.node-gradle.node") version "7.0.2"
}

node {
    download = true
    version = "18.16.0"
}

tasks {
    register("clean") {
        File("$projectDir/build").deleteRecursively()
        File("$projectDir/node_modules").deleteRecursively()
    }
    register("install") {
        dependsOn("npmInstall")
    }
    register("build") {
        dependsOn("npm_run_build")
    }
}

tasks.register("prepareKotlinBuildScriptModel"){}