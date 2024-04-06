#!/bin/bash

cd $(dirname "$(realpath $0)")
cd ../../

pushd ./backend

./gradlew clean build -PwithFrontend || exit 1

popd