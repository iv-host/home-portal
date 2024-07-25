#!/bin/bash
# publish-mvn
# Publishes artifacts to maven through gradle

if [ -z "$MVN_URI" ]; then echo "MVN_URI not set"; exit 1; fi;
if [ -z "$MVN_USERNAME" ]; then echo "MVN_USERNAME not set"; exit 1; fi;
if [ -z "$MVN_PASSWORD" ]; then echo "MVN_PASSWORD not set"; exit 1; fi;

cd $(dirname "$(realpath "$0")")
cd ../../../

./gradlew backend:publish \
  -x jar -x sourcesJar -x assemble -x build \
  -P"mvnUri=$MVN_URI" \
  -P"mvnUsername=$MVN_USERNAME" \
  -P"mvnPassword=$MVN_PASSWORD" \
  || exit 1