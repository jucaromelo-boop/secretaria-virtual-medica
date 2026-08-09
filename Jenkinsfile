pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    environment {
        SONAR_TOKEN = credentials('sonar-token')
        JAVA_HOME = tool 'jdk17'
        MAVEN_HOME = tool 'maven3'
        PATH = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${env.PATH}"
    }

    stages {

        stage('Checkout') {
            steps {
                sh 'cp -r /workspace/* .'
                sh 'ls -la'
            }
        }

        stage('Build') {
            steps {
                sh 'echo "JAVA_HOME=$JAVA_HOME"'
                sh 'echo "PATH=$PATH"'
                sh 'ls -la $JAVA_HOME/bin || echo "JAVA_HOME no es un directorio valido"'
                sh '$JAVA_HOME/bin/java -version'
                sh 'mvn clean compile -B'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test -B'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh 'mvn verify sonar:sonar -Dsonar.projectKey=secretaria-virtual-medica -Dsonar.host.url=http://host.docker.internal:9000 -Dsonar.token=$SONAR_TOKEN -B'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests -B'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker compose build'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completado exitosamente'
        }
        failure {
            echo 'El pipeline fallo - revisar logs'
        }
    }
}