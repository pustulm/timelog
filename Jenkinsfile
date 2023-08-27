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
                    withDockerRegistry(credentialsID: '13b509bb-a471-4344-a588-22e94b5e246a', url:'') {
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
