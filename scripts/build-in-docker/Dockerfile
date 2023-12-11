# Build Envoirnment Container
FROM ubuntu:jammy

# Install System Dependencies
RUN apt-get update \
 && apt-get install -y curl wget

# Install Node
RUN curl -sL https://deb.nodesource.com/setup_18.x  | bash - \
 && apt-get install -y nodejs \
 && curl -L https://www.npmjs.com/install.sh | sh

# Install Java
RUN wget https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-x64_bin.tar.gz \
  && tar -xvf openjdk-17.0.2_linux-x64_bin.tar.gz \
  && mv ./jdk-17.0.2 /usr/local/openjdk-17
ENV JAVA_HOME=/usr/local/openjdk-17
ENV PATH=${JAVA_HOME}/bin:${PATH}

ENTRYPOINT ["npm"]
