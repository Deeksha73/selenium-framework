pipeline {
    agent any

    tools {
        jdk 'JDK26'
        maven 'Maven3'
    }

    stages {
        stage('Run smoke tests') {
            steps {
                sh '''
                    mvn -B clean test \
                        -Dtest=TestRunner \
                        -Dbrowser=chrome \
                        -Dheadless=true \
                        -Dcucumber.filter.tags="@smoke"
                '''
            }
        }
    }

    post {
        always {
            junit testResults: 'target/surefire-reports/TEST-*.xml',
                  allowEmptyResults: true

            archiveArtifacts artifacts: 'target/cucumber-reports.*',
                             allowEmptyArchive: true
        }
    }
}