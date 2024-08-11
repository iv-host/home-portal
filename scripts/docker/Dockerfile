# base image to build a JRE
FROM amazoncorretto:21-alpine as corretto-jdk

# required for strip-debug to work
RUN apk add --no-cache binutils

# Build small JRE image
RUN $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=zip-9 \
         --output /customjre

FROM alpine:latest
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# copy JRE from the base image
COPY --from=corretto-jdk /customjre $JAVA_HOME

RUN apk --no-cache add curl

RUN mkdir -p /app
WORKDIR /app

COPY backend/build/dockerJar/home-portal.jar .
COPY scripts/docker/entrypoint.sh entrypoint.sh

HEALTHCHECK --timeout=5s --start-period=30s \
  CMD curl -f http://localhost:8080/health-check || exit 1

CMD ["sh", "./entrypoint.sh"]
