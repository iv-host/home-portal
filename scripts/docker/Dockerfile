FROM amazoncorretto:17-alpine

RUN apk --no-cache add curl

COPY backend/build/libs/home-portal-*.jar home-portal.jar
COPY scripts/docker/entrypoint.sh entrypoint.sh

HEALTHCHECK --timeout=5s --start-period=30s \
  CMD curl -f http://localhost:8080/health-check || exit 1

CMD ["ash", "./entrypoint.sh"]
