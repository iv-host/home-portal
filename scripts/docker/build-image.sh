#!/bin/bash

cd $(dirname "$(realpath $0)")
docker build --tag home-portal:latest --file Dockerfile ../../ || exit 1

if [ -n "$npm_package_version" ]; then
  docker image rm home-portal:$npm_package_version
  docker tag home-portal:latest home-portal:$npm_package_version
fi