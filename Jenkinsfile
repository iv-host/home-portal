def isSnapshot(String version) {
    return version.endsWith("-SNAPSHOT")
}

node {
    checkout scm
    def projectInfo = readJSON file: 'project.json'

    def buildImage = docker.build("home-portal-build:latest", "./scripts/build-in-docker")

    buildImage.inside {
        stage("build") {
            sh './scripts/build/build.sh'
        }

        stage("publish") {
            withCredentials([usernamePassword(credentialsId: 'mvn-snapshot', usernameVariable: 'MVN_USERNAME', passwordVariable: 'MVN_PASSWORD')]) {
                sh "export MVN_URI=${MVN_URI_SNAPSHOT} && ./scripts/publish-mvn/publish-mvn.sh"
            }
        }
    }

    stage("build-docker") {
        sh "export PROJECT_NAME=${projectInfo["name"]} && export PROJECT_VERSION=${projectInfo["version"]} && scripts/docker/build-image.sh"
    }

    stage("publish-docker") {
        echo "publish"
    }
}
