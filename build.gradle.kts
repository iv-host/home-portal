import java.io.FileWriter

allprojects {
    group = "org.ivcode"
    version = "0.1-SNAPSHOT"
}


tasks {

    register("buildProperties") {
        doLast {
            mkdir("./build")

            FileWriter ("./build/build.properties").use {
                println("name=${project.name}")
                it.write("name=${project.name}\n")

                println("group=${project.group}")
                it.write("group=${project.group}\n")

                println("version=${project.version}")
                it.write("version=${project.version}\n")
            }
        }
    }

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

