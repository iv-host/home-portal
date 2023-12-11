#!/bin/bash

docker image rm home-portal-build
docker build --tag home-portal-build --file Dockerfile ../../