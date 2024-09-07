#!/bin/bash

# Script used to smoke test the application when everything is built together.
#
# Notes:
# - This script depends on the dev environment being up and running.
#

PROJECT_ROOT=$(realpath "$(dirname "$0")/../..")
ENTRYPOINT_SCRIPT="${PROJECT_ROOT}/scripts/docker/entrypoint.sh"

cd "${PROJECT_ROOT}/backend/build/dockerJar" || exit

export "DATABASE_URL=jdbc:mysql://localhost:3306/home?allowPublicKeyRetrieval=true"
export "DATABASE_USERNAME=home"
export "DATABASE_PASSWORD=password"
export "OAUTH2_ENABLED=true"
export "OAUTH2_ADMIN=email(\"admin@domain.com\")"
export "OAUTH2_ISSUER_URL=http://localhost:8081/auth/realms/master"
export "OAUTH2_AUTH_URL=http://localhost:8081/auth/realms/master/protocol/openid-connect/auth"
export "OAUTH2_TOKEN_URL=http://localhost:8081/auth/realms/master/protocol/openid-connect/token"
export "OAUTH2_CLIENT_ID=home-portal"
export "OAUTH2_CLIENT_SECRET=EYm1bCnTp35MN8Phqe7hB97gj3zPwIwJ"

$ENTRYPOINT_SCRIPT
