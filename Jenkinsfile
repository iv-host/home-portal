import groovy.json.JsonSlurper;

node {
    checkout scm
    def projectInfo = JsonSlurper().parse(file("project.json"))

    stage("build-in-docker-image") {
        sh './scripts/build-in-docker/build-image.sh'
    }

    docker.image('home-portal-build:latest').inside {
        stage("build") {
            sh 'npm install'
            sh 'npm run build'
        }

        stage("publish") {
            echo 'publish'
        }
    }

    stage("build-docker") {
        sh "PROJECT_NAME=${projectInfo["name"]} && PROJECT_VERSION=${projectInfo["version"]} && /scripts/docker/build-image.sh"
    }
    stage("publish-docker") {
        sh "publish"
    }
}
