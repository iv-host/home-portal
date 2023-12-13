#!/bin/bash

cd $(dirname "$(realpath $0)")
docker build --tag home-portal --file Dockerfile ../../