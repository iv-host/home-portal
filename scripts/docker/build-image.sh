#!/bin/sh

if [ -z "$PROJECT_NAME" ]; then echo "PROJECT_NAME not set"; exit 1; fi;
if [ -z "$PROJECT_VERSION" ]; then echo "PROJECT_VERSION not set"; exit 1; fi;

cd $(dirname "$(realpath "$0")")
docker build --tag "$PROJECT_NAME":latest --file Dockerfile ../../ || exit 1

docker image rm home-portal:"$PROJECT_VERSION"
docker tag home-portal:latest home-portal:"$PROJECT_VERSION"