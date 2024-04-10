#!/bin/bash
# build
# Builds the project

cd $(dirname "$(realpath "$0")")
cd ../../../

pushd ./backend

./gradlew clean build -PwithFrontend || exit 1

popd