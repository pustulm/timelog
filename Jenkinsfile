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
                container('kubectl') {
                  withCredentials([file(credentialsId: 'mykubeconfig', variable: 'KUBECONFIG')]) {
                        sh 'sed -i "s/<TAG>/${BUILD_NUMBER}/" myweb.yaml'
                        sh 'kubectl apply -f myweb.yaml'
                )
            }
        }
    }
}
