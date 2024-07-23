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
    def isPublishToMaven = params['Publish To Maven'] ?: false
    def isPublishToDocker = params['Publish To Docker'] ?: false

    echo "Publish To Maven: ${isPublishToMaven}"
    echo "Publish To Docker: ${isPublishToDocker}"

    checkout scm

    def buildImg = docker.build("home-portal-build:latest", "./scripts/jenkins/build-in-docker")

    projectName = null
    projectVersion = null

    buildImg.inside {
        stage("prepare") {
            // start the daemon
            sh './gradlew info --daemon'

            projectName = sh (
                script: './gradlew info_name -q',
                returnStdout: true
            ).trim()

            projectVersion = sh (
               script: './gradlew info_version -q',
               returnStdout: true
            ).trim()

            echo "${projectName}:${projectVersion}"
        }

        stage("build") {
            sh './scripts/jenkins/build/build.sh'
        }

        if (env.BRANCH_NAME == 'main') {
            stage("publish") {
                withCredentials([usernamePassword(credentialsId: 'mvn-snapshot', usernameVariable: 'MVN_USERNAME', passwordVariable: 'MVN_PASSWORD')]) {
                    sh "export MVN_URI=${MVN_URI_SNAPSHOT} && ./scripts/jenkins/publish-mvn/publish-mvn.sh";
                }

                if(!isSnapshot(projectVersion)) {
                    throw new UnsupportedOperationException("release process not yet defined")
                }
            }
        }
    }

    stage("build-docker") {
        docker.build("${projectName}:${projectVersion}", "--file ./scripts/jenkins/docker/Dockerfile .")
    }

    if (env.BRANCH_NAME == 'main') {
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

// Define parameters in the job configuration
properties([
    parameters([
        booleanParam(name: 'Publish To Maven', defaultValue: env.BRANCH_NAME == 'main', description: 'publishes artifacts to the maven repository'),
        booleanParam(name: 'Publish To Docker', defaultValue: env.BRANCH_NAME == 'main', description: 'publishes the docker image to the docker repository')
    ])
])
