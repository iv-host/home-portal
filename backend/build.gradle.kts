buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("be.filipblondeel.gradle:gradle-gulp-plugin:0.1")
    }
}
plugins {
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.spring") version "1.8.20"
    id("org.springframework.boot") version "3.1.6"
    id("io.spring.dependency-management") version "1.1.0"
    application
}

group = "org.ivcode"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.apache.commons:commons-lang3")

    implementation("org.mybatis:mybatis:3.5.14")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")

    implementation("mysql:mysql-connector-java:8.0.33")

    testImplementation(kotlin("test"))
}

val frontendDirectory = File(layout.projectDirectory.asFile.parentFile, "frontend")
val publicDirectory = layout.buildDirectory.dir("resources/main/public").get().asFile

fun String.runCommand(workingDirectory: File = layout.projectDirectory.asFile): Int {
    println("> $this")
    return project.exec {
        workingDir = workingDirectory
        commandLine = this@runCommand.split("\\s".toRegex())
        //standardOutput = System.out
        //errorOutput = System.err
    }.exitValue
}

if(project.hasProperty("withFrontend")) {
    tasks.named("jar").configure{ dependsOn("copy-frontend") }
    tasks.named("bootJar").configure{ dependsOn("copy-frontend") }
    tasks.named("resolveMainClassName").configure{ dependsOn("copy-frontend") }

    tasks.register("build-frontend") {
        doLast {
            "npm run build".runCommand(frontendDirectory)
        }
    }

    tasks.register<Copy>("copy-frontend") {
        dependsOn(":processResources",":build-frontend")
        from(file(File(frontendDirectory,"build").absolutePath))
        into(publicDirectory.absolutePath)
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("org.ivcode.homeportal.MainKt")
}