/*
 * Jenkins Build Script
 *
 * Jenkins Plugins:
 *   - Docker Pipeline
 *   - Pipeline Utility Steps
 *
 * Environmental Variables:
 *   - MVN_URI_SNAPSHOT: The uri to the snapshot repository
 *   - DOCKER_URI_SNAPSHOT: The uri to the snapshot registry
 *
 * Credentials:
 *   - mvn-snapshot: basic auth credentials for publishing artifacts to mvn
 *   - docker-snapshot: basic auth credentials for publishing artifacts to docker
 */

def isSnapshot(String version) {
    return version.endsWith("-SNAPSHOT")
}

node {
    checkout scm

    def projectInfo = readJSON file: 'project.json'
    def projectName = projectInfo["name"]
    def projectVersion = projectInfo["version"]

    withEnv([
        "PROJECT_NAME=${projectName}",
        "PROJECT_VERSION=${projectVersion}"
    ]) {
        echo '$PROJECT_NAME:$PROJECT_VERSION'

        docker.image('node:18').inside {
            stage("build-frontend") {
                sh './scripts/jenkins/build-frontend/build.sh'
            }
        }

        docker.image("amazoncorretto:21-alpine").inside {
            stage("build-backend") {
                sh './scripts/jenkins/build/build.sh'
            }

            stage("publish") {
                withCredentials([usernamePassword(credentialsId: 'mvn-snapshot', usernameVariable: 'MVN_USERNAME', passwordVariable: 'MVN_PASSWORD')]) {
                    sh "export MVN_URI=${MVN_URI_SNAPSHOT} && ./scripts/jenkins/publish-mvn/publish-mvn.sh";
                }

                if(!isSnapshot(projectVersion)) {
                    throw new UnsupportedOperationException("release process not yet defined")
                }
            }
        }

        stage("build-docker") {
            docker.build("${projectName}:${projectVersion}", "--file ./scripts/jenkins/docker/Dockerfile .")
        }

        stage("publish-docker") {
            docker.withRegistry(env.DOCKER_URI_SNAPSHOT, 'docker-snapshot') {
                def image = docker.build("${projectName}", "--file ./scripts/jenkins/docker/Dockerfile .")
                image.push(projectVersion)
                image.push("latest")
            }

            if(!isSnapshot(projectVersion)) {
                throw new UnsupportedOperationException("release process not yet defined")
            }
        }
    }
}
