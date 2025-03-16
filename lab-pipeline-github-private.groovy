pipeline {
  agent none
  environment {
    AWS_CREDENTIALS = 'aws_s3_admin_1_username_with_password'
    AWS_REGION = 'ap-southeast-1'
    S3_BUCKET = 'mybucket'
    LOCAL_PATH = 'dist/'  // Local folder to upload
  }
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
            nodejs('22') {
              sh "ls"
              sh "find . -name 'dist' -type d -exec rm -rf {} +"
              sh "find . -name 'node_modules' -type d -exec rm -rf {} +"
              sh "node --version"
              sh "npm install"
              sh "npm run build"
            }
          }
        }
      }
    }
    stage('Prune') {
      agent any
      steps {
        echo 'prune...'
      }
    }
    stage('Checkout Deployment') {
      agent any
      steps {
        echo 'Checkout Deployment...'
      }
    }
    stage('Deployment') {
      agent any
      steps {
        script {
          if (params.blog) {
            withAWS(credentials: AWS_CREDENTIALS, region: AWS_REGION) {
              s3Upload(
                bucket: S3_BUCKET,
                file: LOCAL_PATH,
                path: '', // Upload to root path
                workingDir: ''
              )
            }
          }
        }
      }
    }
  }
}
