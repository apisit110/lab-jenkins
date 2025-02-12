pipeline {
  agent none
  parameters {
    gitParameter branch: '',
                 branchFilter: 'origin/release/(.*)',
                 defaultValue: '1.0.0',
                 description: '',
                 name: 'release',
                 quickFilterEnabled: false,
                 selectedValue: 'NONE',
                 sortMode: 'DESCENDING_SMART',
                 tagFilter: '*',
                 type: 'PT_BRANCH',
                 listSize: '1'
    booleanParam(name: 'blog', defaultValue: false, description: '')
  }
  stages {
    stage('Checkout') {
      agent any
      steps {
        checkout([
            $class: 'GitSCM',
            branches: [[name: "release/${params.release}"]],
            doGenerateSubmoduleConfigurations: false,
            extensions: [],
            gitTool: 'Default',
            submoduleCfg: [],
            userRemoteConfigs: [[credentialsId: 'github_personal_access_tokens_1_username_with_password', url: 'https://github.com/apisit110/blog.git']]
        ])
      }
    }
    stage('Build') {
      agent any
      steps {
        script {
          if (params.blog) {
            sh "DOCKER_BUILDKIT=1 docker build -t blog:${params.release} -f apps/blog/Dockerfile ."
          }
        }
      }
    }
    stage('Prune') {
      agent any
      steps {
        sh 'docker image prune -a -f'
      }
    }
    stage('Checkout Deployment') {
      steps {
        echo 'Checkout Deployment...'
      }
    }
    stage('Deployment') {
      steps {
        echo 'Deployment...'
      }
    }
  }
}
