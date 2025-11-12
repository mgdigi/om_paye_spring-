pipeline {
    agent { label 'java' }

    environment {
        DOCKERHUB_REPO = 'mgdigi/ompaye_spring'
    }

    stages {



        stage('Build Docker image') {
            steps {
                sh 'docker build -t $DOCKERHUB_REPO:latest .'
            }
        }

        stage('Login & Push Docker image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub_credential', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $DOCKERHUB_REPO:latest
                    '''
                }
            }
        }

        // stage('Deploy to Render') {
        //     steps {
        //         withCredentials([string(credentialsId: 'render-deploy-hook', variable: 'RENDER_HOOK_URL')]) {
        //             sh '''
        //                 curl -X POST $RENDER_HOOK_URL
        //             '''
        //         }
        //     }
        // }


    }

    post {
        success { echo '✅ Build and push and deploy succeeded!' }
        failure { echo '❌ Build failed.' }
    }
}