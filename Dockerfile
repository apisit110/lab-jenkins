FROM jenkins/jenkins
USER root
RUN apt-get update \
  && apt-get install ca-certificates curl
# RUN install -m 0755 -d /etc/apt/keyrings
RUN curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc
# RUN chmod a+r /etc/apt/keyrings/docker.asc
RUN echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/debian \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  tee /etc/apt/sources.list.d/docker.list > /dev/null
RUN apt-get update \
  && apt-get -y install docker-ce
# docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
# RUN usermod -aG docker jenkins

# docker build -t lab-jenkins-custom:0.0.0 .
