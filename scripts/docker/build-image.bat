cd %~dp0
docker build --tag home-portal:latest --file Dockerfile ..\\..\\ || EXIT /B 1

IF defined npm_package_version (
  docker image rm home-portal:%npm_package_version%
  docker tag home-portal:latest home-portal:%npm_package_version%
)