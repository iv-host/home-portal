FROM openjdk:17-jdk-slim

COPY backend/build/libs/home-portal-*.jar home-portal.jar
COPY scripts/docker/entrypoint.sh entrypoint.sh

CMD ["./entrypoint.sh"]