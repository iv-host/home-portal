#!/bin/bash

cd $(dirname "$(realpath $0)")
cd ../../
docker run --rm --workdir /build -v ${PWD}/:/build home-portal-build
