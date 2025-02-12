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
    booleanParam(name: 'portal', defaultValue: false, description: '')
    booleanParam(name: 'portal_service', defaultValue: false, description: '')
    booleanParam(name: 'batch', defaultValue: false, description: '')
    booleanParam(name: 'queue_processor', defaultValue: false, description: '')
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
            userRemoteConfigs: [[url: 'https://github.com/apisit110/monorepo.git']]
        ])
      }
    }
    stage('Build') {
      agent any
      steps {
        script {
          if (params.portal) {
            sh "DOCKER_BUILDKIT=1 docker build -t mylab-portal:${params.release} -f apps/mylab-portal/Dockerfile ."
          }
          if (params.portal_service) {
            sh "DOCKER_BUILDKIT=1 docker build -t mylab-portal-service:${params.release} -f apps/mylab-portal-service/Dockerfile ."
            echo 'push ...'
          }
          if (params.batch) {
            sh "DOCKER_BUILDKIT=1 docker build -t mylab-batch:${params.release} -f apps/mylab-batch/Dockerfile ."
          }
          if (params.queue_processor) {
            sh "DOCKER_BUILDKIT=1 docker build -t mylab-golang-service:${params.release} -f apps/mylab-golang-service/Dockerfile ."
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
