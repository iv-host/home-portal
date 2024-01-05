#!/bin/bash

cd $(dirname "$(realpath $0)")
docker build --tag home-portal:latest --file Dockerfile ../../

[ ! -z "$npm_package_version" ] && docker tag home-portal:latest home-portal:$npm_package_version