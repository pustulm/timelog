pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("timelog-api:${env.BUILD_NUMBER}")
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                kubernetesDeploy(
                    kubeconfigId: 'YOUR_KUBECONFIG_ID',
                    configs: 'timelog-deployment.yaml',
                    enableConfigSubstitution: true
                )
            }
        }
    }
}
