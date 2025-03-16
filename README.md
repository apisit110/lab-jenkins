github private repo

to pull private repo we have 2 options

1. Set SSH Key

- go to your account Settings -> SSH and GPG keys
- generate key with command ssh-keygen
- add public key to github
- jenkins add credential type SSH Username with private key


2. Set Personal access tokens

can config the permission to access

- go to your account Settings -> Developer settings -> Personal access tokens
- generate new token with permission set up
- jenkins add credential type username and password got from github

-----

to run the jenkins

1. Create Dockerfile to build custom docker images jenkins that installed docker software to run docker-in-docker
2. Run images with commard or Create docker compose
3. when jenkins started install some plugin
   - Git Parameter to checkout git server
   - HTTP Request to send http
   - Docker Pipeline to push docker images to registry
   - Pipeline: Stage View to show stage view build history
   - NodeJS use node version in pipeline setting in Management Jenkins -> Tools [NodeJS installations][Add]
   - Pipeline: AWS Steps to use aws
4. Add your credential to jenkins

Ready to create pipeline

-----
Use Jenkins UsernamePassword credentials information (Username: AccessKeyId, Password: SecretAccessKey):

Use Jenkins AWS credentials information (AWS Access Key: AccessKeyId, AWS Secret Key: SecretAccessKey):

```groovy
withAWS(credentials:'IDofSystemCredentials') {
    // do something
}
```
-----

to disable jenkins password

vim /var/jenkins_home/config.xml
  
<useSecurity>false</useSecurity>
