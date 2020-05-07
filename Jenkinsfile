pipeline{
    agent any

    environment{
        MAVEN_CREDENTIAL = credentials('heartpattern-maven-repository')
    }

    stages{
        stage('test'){
            steps{
                sh './gradlew test'
            }
        }
        stage('create plugin'){
            steps{
                sh './gradlew createPlugin -x test'
            }
        }
        stage('publish'){
            steps{
                sh './gradlew publish -x test'
            }
        }
    }

    post{
        always{
            archiveArtifacts artifacts: 'build/libs/*', fingerprint: true
        }
    }
}