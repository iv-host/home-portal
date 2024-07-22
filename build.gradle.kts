allprojects {
    group = "org.ivcode"
    version = "0.1-SNAPSHOT"
}


tasks {
    register("info_version") {
        println(project.version)
    }
    register("info_name") {
        println(project.name)
    }
}



tasks.register("clean") {
    dependsOn(":frontend:clean", ":backend:clean")
}


tasks.register("build") {
    dependsOn(":frontend:build", ":backend:build")
}
