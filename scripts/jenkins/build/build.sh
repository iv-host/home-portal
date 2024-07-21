#!/bin/bash
# build
# Builds the project

cd $(dirname "$(realpath "$0")")
cd ../../../

./gradlew clean build || exit 1
