/*
 * Jenkins Build Script
 *
 * Jenkins Plugins:
 *   - Docker Pipeline
 *   - Pipeline Utility Steps
 *   - Build With Parameters
 *
 * Environmental Variables:
 *   - MVN_URI_SNAPSHOT: The uri to the snapshot repository
 *   - DOCKER_URI_SNAPSHOT: The uri to the snapshot registry
 *
 * Credentials:
 *   - mvn-snapshot: basic auth credentials for publishing artifacts to mvn
 *   - docker-snapshot: basic auth credentials for publishing artifacts to docker
 */
import org.jenkinsci.plugins.pipeline.modeldefinition.Utils

def isSnapshot(String version) {
    return version.endsWith("-SNAPSHOT")
}

def isMainBranch() {
    return env.BRANCH_NAME == 'main'
}

node {
    def isPublishToMaven = params['publish maven'] ?: false
    def isPublishToDocker = params['publish docker'] ?: false

    echo "publish maven: ${isPublishToMaven}"
    echo "publish docker: ${isPublishToDocker}"

    checkout scm

    def buildImg = docker.image("registry.ivcode.org/corretto-ubuntu:21-jammy")
    def props = null

    buildImg.inside {
        stage("prepare") {
            // start the daemon
            sh './gradlew buildProperties --daemon'

            // load the properties
            props = readProperties file: 'build/build.properties'

            echo "Project Name: ${props.name}"
            echo "Project Version: ${props.version}"
        }

        stage("build") {
            sh './gradlew clean build'
        }

        stage("publish maven") {
            if(!isPublishToMaven) {
                Utils.markStageSkippedForConditional(STAGE_NAME)
                return
            }

            withEnv(["MVN_URL=${MVN_URI_SNAPSHOT}"]) {
                withCredentials([usernamePassword(credentialsId: 'mvn-snapshot', usernameVariable: 'MVN_USERNAME', passwordVariable: 'MVN_PASSWORD')]) {
                    sh './gradlew publish'
                }
            }

            if(!isSnapshot(props.version)) {
                throw new UnsupportedOperationException("release process not yet defined")
            }
        }
    }

    def dockerBuildImage = null
    stage("build docker") {
        dockerBuildImage = docker.build(props.name, "--file ./scripts/jenkins/docker/Dockerfile .")
    }

    stage("publish docker") {
        if(!isPublishToDocker) {
            Utils.markStageSkippedForConditional(STAGE_NAME)
            return
        }

        docker.withRegistry(env.DOCKER_URI_SNAPSHOT, 'docker-snapshot') {
            dockerBuildImage.push(props.version)
            dockerBuildImage.push("latest")
        }

        if(!isSnapshot(props.version)) {
            throw new UnsupportedOperationException("release process not yet defined")
        }
    }
}

// Define parameters in the job configuration
properties([
    parameters([
        booleanParam(name: 'publish maven', defaultValue: env.BRANCH_NAME == 'main', description: 'publish to the maven repository'),
        booleanParam(name: 'publish docker', defaultValue: env.BRANCH_NAME == 'main', description: 'publish to the docker repository')
    ])
])
