node {
    checkout scm

    stage("build-in-docker-image") {
        sh './scripts/build-in-docker/build-image.sh'
    }

    stage("build-in-docker") {
        docker.image('home-portal-build:latest').inside {
            sh './scripts/build/build.sh'
        }
    }
}
