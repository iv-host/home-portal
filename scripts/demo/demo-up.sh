#!/bin/bash

STARTUP_ATTEMPT=0
STARTUP_MAX_ATTEMPTS=10
STARTUP_WAIT_TIME=5

cd $(dirname "$(realpath $0)")

docker-compose up -d || exit 1;

# wait for the application to become healthy and open in browser
while [ "$STARTUP_ATTEMPT" -lt "$STARTUP_MAX_ATTEMPTS" ]; do
  ((STARTUP_ATTEMPT=STARTUP_ATTEMPT+1))
  echo "waiting for service (Healthcheck Attempt $STARTUP_ATTEMPT of $STARTUP_MAX_ATTEMPTS)"
  sleep "$STARTUP_WAIT_TIME"s

  # Open the application in browser if the health-check passes
  if curl -f http://localhost:8080/health-check; then
    xdg-open http://localhost:8080 || exit 1;
    exit 0;
  fi;

done;

echo "Startup Failed... Check your docker instance."