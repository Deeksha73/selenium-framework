pipeline {
    agent any

    tools {
        jdk 'JDK26'
        maven 'Maven3'
    }
    options {
        disableConcurrentBuilds()

        timeout(time: 10, unit: 'MINUTES')

        buildDiscarder(logRotator(
            numToKeepStr: '20',
            artifactNumToKeepStr: '10'
        ))
    }
    triggers {
        pollSCM('H/5 * * * *')
    }

    parameters {
        string(
            name: 'CUCUMBER_TAGS',
            defaultValue: '@regression',
            description: 'Leave empty for all scenarios, or enter @smoke, @login, @cart, or a tag expression.',
            trim: true
        )
        choice(
            name: 'PARALLEL_THREADS',
            choices: ['2', '1'],
            description: 'Maximum number of scenarios running concurrently.'
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
                        "-Dcucumber.filter.tags=$CUCUMBER_TAGS" \
                        "-Dparallel.threads=$PARALLEL_THREADS"
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
            publishHTML(target: [
                  reportDir: 'target',
                  reportFiles: 'cucumber-reports.html',
                  reportName: 'Cucumber Report',
                  includes: 'cucumber-reports.html',
                  keepAll: true,
                  alwaysLinkToLastBuild: true,
                  allowMissing: false
            ])
        }
    }
}