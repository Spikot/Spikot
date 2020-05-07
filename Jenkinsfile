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
                sh './gradlew publish -x test -Pmaven.user=${MAVEN_CREDENTIAL_USR} -Pmaven.password=${MAVEN_CREDENTIAL_PSW}'
            }
        }
    }

    post{
        always{
            archiveArtifacts artifacts: 'build/libs/*', fingerprint: true
        }
    }
}