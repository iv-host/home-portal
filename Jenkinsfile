node {
    checkout scm
    sh 'nvm use 18'

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
        sh "npm run build-image"
    }
    stage("publish-docker") {
        sh "publish"
    }
}
