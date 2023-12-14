## Build Liquibase Dependency
FROM ubuntu:jammy

RUN apt-get update \
 && apt-get install -y wget

RUN mkdir /liquibase
WORKDIR /liquibase

RUN wget https://github.com/liquibase/liquibase/releases/download/v4.25.0/liquibase-4.25.0.tar.gz \
  && tar -xf liquibase-4.25.0.tar.gz \
  && rm liquibase-4.25.0.tar.gz
RUN cd internal/lib \
  && wget https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar

## Main Image
FROM openjdk:17-jdk-slim

COPY backend/build/libs/home-portal-*.jar home-portal.jar
COPY scripts/docker/entrypoint.sh entrypoint.sh

COPY --from=0 /liquibase /liquibase
COPY ./database /database

CMD ["./entrypoint.sh"]
