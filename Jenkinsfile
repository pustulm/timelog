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
                    dockerImage = docker.build("michalp96/timelog-app:${env.BUILD_NUMBER}")
                }
            }
        }
        
        stage('Pushing Image') {
            steps {
                script {
                    docker.withRegistry( '', 'dockerhub' ) {
                        dockerImage.push('latest')
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                withKubeConfig(credentialsId: 'kubernetes', serverUrl: '') {
                         sh 'kubectl apply -f timelog-deployment.yaml'
                }        
            }  
        }
    }
}
