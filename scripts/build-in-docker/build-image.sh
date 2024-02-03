#!/bin/bash

cd $(dirname "$(realpath $0)")
docker image rm home-portal-build
docker build --tag home-portal-build --file Dockerfile ../../ || exit 1