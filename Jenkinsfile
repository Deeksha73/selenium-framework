pipeline {
    agent any

    tools {
        jdk 'JDK26'
        maven 'Maven3'
    }

    parameters {
        string(
            name: 'CUCUMBER_TAGS',
            defaultValue: '@smoke',
            description: 'Leave empty for all scenarios, or enter @smoke, @login, @cart, or a tag expression.',
            trim: true
        )
    }
    stages {
        stage('Run tests') {
            steps {
                sh '''
                    mvn -B clean test \
                        -Dtest=TestRunner \
                        -Dbrowser=chrome \
                        -Dheadless=true \
                        "-Dcucumber.filter.tags=$CUCUMBER_TAGS"
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