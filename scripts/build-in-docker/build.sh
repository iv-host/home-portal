#!/bin/bash

cd $(dirname "$(realpath $0)")
cd ../../
docker run \
  --rm \
  --workdir /build \
  -v ${PWD}/:/build \
  -e npm_package_version=${npm_package_version} \
  home-portal-build

docker image rm home-portal-build