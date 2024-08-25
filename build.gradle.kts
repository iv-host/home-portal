plugins {
    id("org.ivcode.gradle-info") version "0.1-SNAPSHOT"
}

allprojects {
    group = "org.ivcode"
    version = "0.1-SNAPSHOT"
}


tasks {
    // --== Build Tasks ==--
    register("clean") {
        dependsOn(":frontend:clean", ":backend:clean")
        File("./build").deleteRecursively()
    }
    register("build") {
        dependsOn(":frontend:build", ":backend:build")
    }
    register("publish") {
        dependsOn(":backend:publish")
    }

    // --== Docker Tasks ==--
    // Start the dev environment
    register("dev-up", Exec::class) {
        workingDir = File(projectDir, "scripts${File.separator}dev")
        if(!workingDir.exists()) {
            throw GradleException("Directory does not exist: ${workingDir.absolutePath}")
        }
        commandLine("docker", "compose", "up", "-d")
    }
    // Stop the dev environment
    register("dev-down", Exec::class) {
        workingDir = File(projectDir, "scripts${File.separator}dev")
        if(!workingDir.exists()) {
            throw GradleException("Directory does not exist: ${workingDir.absolutePath}")
        }
        commandLine("docker", "compose", "down")
    }
}
