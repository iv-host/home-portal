cd %~dp0
cd ..\..\

docker run ^
  --rm ^
  --workdir /build ^
  -v %cd%\:/build ^
  -e npm_package_version=%npm_package_version% ^
  home-portal-build

docker image rm home-portal-build