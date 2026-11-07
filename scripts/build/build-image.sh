#!/bin/sh
set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
PROJECT_ROOT=$(CDPATH= cd -- "$SCRIPT_DIR/../.." && pwd)
BUILD_IMAGE=home-portal-build
APP_IMAGE=${1:-home-portal:local}
DOCKER_PROJECT_ROOT=$PROJECT_ROOT

if command -v cygpath >/dev/null 2>&1; then
  DOCKER_PROJECT_ROOT=$(cygpath -m "$PROJECT_ROOT")
fi

docker build \
  --tag "$BUILD_IMAGE" \
  --file "$SCRIPT_DIR/Dockerfile" \
  "$SCRIPT_DIR"

MSYS_NO_PATHCONV=1 docker run --rm \
  --mount "type=bind,source=$DOCKER_PROJECT_ROOT,target=/workspace" \
  --workdir=/workspace \
  "$BUILD_IMAGE" \
  sh ./gradlew :backend:dockerJar --no-daemon

docker build \
  --file "$PROJECT_ROOT/scripts/docker/Dockerfile" \
  --tag "$APP_IMAGE" \
  "$PROJECT_ROOT"
