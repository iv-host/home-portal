plugins {
    application
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.ivcode.gradle-publish") version "0.1-SNAPSHOT"
}

java {
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")

    // Apache Commons
    implementation("org.apache.commons:commons-lang3")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")

    // Liquibase
    implementation("org.liquibase:liquibase-core:4.25.1")

    // MyBatis
    implementation("org.mybatis:mybatis:3.5.14")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")

    // DB Drivers
    implementation("com.mysql:mysql-connector-j:8.2.0")
    implementation("com.h2database:h2:2.2.224")


    // Test
    testImplementation(kotlin("test"))
    testImplementation("org.mockito:mockito-core:5.8.0")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}


val frontendDirectory = File(layout.projectDirectory.asFile.parentFile, "frontend")
val resourcesDirectory = layout.buildDirectory.dir("resources").get().asFile
val publicDirectory = File(resourcesDirectory, "main/public")

// Update task chain to include new tasks
tasks.named("test").configure{ dependsOn("copy-frontend") }
tasks.named("test").configure{ dependsOn("write-version") }
tasks.named("bootJar").configure{ dependsOn("copy-frontend") }
tasks.named("bootJar").configure{ dependsOn("write-version") }
tasks.named("resolveMainClassName").configure{ dependsOn("copy-frontend") }
tasks.named("resolveMainClassName").configure{ dependsOn("write-version") }

// Copy frontend to the build's /resources/public
tasks.register<Copy>("copy-frontend") {
    dependsOn(":frontend:build",":backend:processResources")
    from(file(File(frontendDirectory,"build").absolutePath))
    into(publicDirectory.absolutePath)
}

// Write version to the build's /resources
tasks.register("write-version") {
    dependsOn(":backend:processResources")
    doLast {
        File(resourcesDirectory, "main/version.txt").writeText(text = version as String)
    }
}

// Copy frontend to the build's /resources/public
tasks.named("jar").configure{ dependsOn("dockerJar") }
tasks.register<Copy>("dockerJar") {
    dependsOn("bootJar")

    val libsPath = layout.buildDirectory.dir("libs").get().asFile
    val appPath = layout.buildDirectory.dir("dockerJar").get().asFile
    val appName = "${project.name}-${project.version}.jar"

    from(file(File(libsPath, appName).absolutePath))
    into(file(appPath.absolutePath))

    rename{ filename ->
        if(filename == appName) {
            "${rootProject.name}.jar"
        } else {
            filename
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    explicitApi()
    jvmToolchain(21)
}

application {
    mainClass.set("org.ivcode.homeportal.MainKt")
}