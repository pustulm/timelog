pipeline {
    
    environment {
        dockerImageName = 'michalp96/timelog-app'
        dockerImage = ''
    }
    
    agent any

    stages {
        stage('Checkout source') {
            steps {
                git 'https://github.com/pustulm/timelog.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("timelog-api:${env.BUILD_NUMBER}")
                }
            }
        }
        
        stage('Pushing Image') {
            steps {
                script {
                    docker.withRegistry( 'https://hub.docker.com/repository/docker/michalp96/timelog-app/general', 'dockerhub' ) {
                        dockerImage.push('latest')
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                kubernetesDeploy(
                    configs: 'timelog-deployment.yaml',
                    enableConfigSubstitution: true
                )
            }
        }
    }
}
