#!/bin/bash
# build-frontend
# Builds the project's frontend

if [ -z "$PROJECT_NAME" ]; then echo "PROJECT_NAME not set"; exit 1; fi;
if [ -z "$PROJECT_VERSION" ]; then echo "PROJECT_VERSION not set"; exit 1; fi;

cd $(dirname "$(realpath "$0")")
cd ../../../

pushd ./frontend

# Clean
rm -rf ./build
rm -rf ./node_modules

# Setup
export npm_package_name=$PROJECT_NAME
export npm_package_version=$PROJECT_VERSION

# Build
npm install && npm run build

popd