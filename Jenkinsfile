node {
    checkout scm

    stage("build-in-docker-image") {
        sh './scripts/build-in-docker/build-image.sh'
    }

    docker.image('home-portal-build:latest').inside {

        stage("build-in-docker") {
            sh './scripts/build/build.sh'
        }

        stage("publish") {
            echo 'publish'
        }
    }

}
