#!/bin/bash

cd $(dirname "$(realpath $0)")
cd ../../

pushd ./frontend
npm run clean
popd

pushd ./backend
./gradlew clean build -PwithFrontend
popd