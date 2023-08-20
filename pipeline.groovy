pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials')
        KUBECONFIG_CREDENTIALS = credentials('kubeconfig-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.withRegistry('', DOCKER_HUB_CREDENTIALS) {
                        def customImage = docker.build("your-docker-username/time-log-app:${BUILD_NUMBER}")
                        customImage.push()
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    kubeConfigFile = writeKubeConfigToFile()
                    kubernetesDeploy(
                        kubeconfigId: KUBECONFIG_CREDENTIALS,
                        configs: kubeConfigFile
                    )
                }
            }
        }
    }
}

def writeKubeConfigToFile() {
    def kubeConfigContent = credentials('kubeconfig-credentials')
    def kubeConfigFile = 'kubeconfig.yml'
    writeFile file: kubeConfigFile, text: kubeConfigContent
    return kubeConfigFile
}
