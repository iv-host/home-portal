#!/bin/bash

if [ -z "$MVN_URI" ]; then echo "PROJECT_NAME not set"; exit 1; fi;
if [ -z "$MVN_USERNAME" ]; then echo "PROJECT_NAME not set"; exit 1; fi;
if [ -z "$MVN_PASSWORD" ]; then echo "PROJECT_NAME not set"; exit 1; fi;

cd $(dirname "$(realpath "$0")")
cd ../../

pushd ./backend

./gradlew publish \
  -x jar -x sourcesJar -x assemble -x build \
  -PmvnUri="$MVN_URI" \
  -PmvnUsername="$MVN_USERNAME" \
  -PmvnPassword="$MVN_PASSWORD" \
  || exit 1

popd