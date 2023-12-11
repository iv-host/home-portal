#!/bin/bash

cd ../../
docker run --rm --workdir /build -v ${PWD}/:/build home-portal-build run clean