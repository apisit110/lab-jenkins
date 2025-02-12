pipeline {
  agent any
  stages {
    stage('Global variable') {
      steps {
        echo "${env}"
        echo "${params}"
        echo "${currentBuild}"
        echo "${BUILD_NUMBER}"
        say("hello")
      }
    }
    stage('Call http request') {
      steps {
        script {
          def response = httpRequest acceptType: 'APPLICATION_JSON', contentType: 'APPLICATION_JSON',
                                    httpMode: 'POST', quiet: true,
                                    requestBody: '''{
                                      "msg_type": "text",
                                      "content": {
                                        "text": "request example"
                                      }
                                    }''',
                                    url: "${env.LARK_NOTIFY_LAB}"
          echo "${response}"
        }
      }
    }
  }
}
def say(message) {
  echo "${message}"
}