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
    echo "${projectName}:${projectVersion}"

    def buildImage = docker.build("home-portal-build:latest", "./scripts/build-in-docker")

    buildImage.inside {
        stage("build") {
            sh './scripts/build/build.sh'
        }

        stage("publish") {
            withCredentials([usernamePassword(credentialsId: 'mvn-snapshot', usernameVariable: 'MVN_USERNAME', passwordVariable: 'MVN_PASSWORD')]) {
                sh "export MVN_URI=${MVN_URI_SNAPSHOT} && ./scripts/publish-mvn/publish-mvn.sh"
            }

            if(!isSnapshot(projectVersion) {
                throw new UnsupportedOperationException("release not yet defined")
            }
        }
    }

    stage("build-docker") {
        docker.build("${projectName}:${projectVersion}", "./scripts/docker")
    }

    stage("publish-docker") {
        docker.withRegistry(env.DOCKER_URI_SNAPSHOT, 'docker-snapshot') {
            docker.build("${projectName}:${projectVersion}", "./scripts/docker").push()
            docker.build("${projectName}:latest", "./scripts/docker").push()
        }

        if(!isSnapshot(projectVersion) {
            throw new UnsupportedOperationException("release not yet defined")
        }
    }
}
