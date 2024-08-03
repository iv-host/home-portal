allprojects {
    group = "org.ivcode"
    version = "0.1-SNAPSHOT"
}


tasks {
    // Info Tasks
    register("info") {
        doLast { println("${project.name}:${project.version}") }
    }
    register("info_version") {
        doLast { println(project.version)}
    }
    register("info_name") {
        doLast { println(project.name) }
    }

    // Build Tasks
    register("clean") {
        dependsOn(":frontend:clean", ":backend:clean")
    }
    register("build") {
        dependsOn(":frontend:build", ":backend:build")
    }
    register("publish") {
        dependsOn(":backend:publish")
    }
}

