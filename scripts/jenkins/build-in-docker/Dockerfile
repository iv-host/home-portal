# Build Environment Container
FROM ubuntu:jammy

# Install System Dependencies
RUN apt-get update \
 && apt-get install -y wget gpg

# Add Corretto Repository and GPG key
RUN wget -O - https://apt.corretto.aws/corretto.key | gpg --dearmor -o /usr/share/keyrings/corretto-keyring.gpg \
  && echo "deb [signed-by=/usr/share/keyrings/corretto-keyring.gpg] https://apt.corretto.aws stable main" | tee /etc/apt/sources.list.d/corretto.list

# Install JDK
RUN apt-get update \
  && apt-get install -y java-21-amazon-corretto-jdk
