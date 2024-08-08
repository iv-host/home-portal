plugins {
    id("org.ivcode.gradle-info") version "0.1-SNAPSHOT"
}

allprojects {
    group = "org.ivcode"
    version = "0.1-SNAPSHOT"
}


tasks {
    // Build Tasks
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
}

