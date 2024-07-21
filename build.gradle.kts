allprojects {
    group = "org.ivcode"
    version = "0.1-SNAPSHOT"
}

tasks.register("clean") {
    dependsOn(":frontend:clean", ":backend:clean")
}

tasks.register("build") {
    dependsOn(":frontend:build", ":backend:build")
}