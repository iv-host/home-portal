#!/bin/bash

cd $(dirname "$(realpath $0)")
cd ../../

pushd ./backend
./gradlew clean build -PwithFrontend

if [ $? -ne 0 ]; then
  exit 1
fi

popd