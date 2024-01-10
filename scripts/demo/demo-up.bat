cd %~dp0

SET STARTUP_ATTEMPT=0
SET STARTUP_MAX_ATTEMPTS=10
SET STARTUP_WAIT_TIME=5

docker-compose up -d || EXIT /B 1

:: wait for the application to become healthy and open in browser
:healthcheck_loop
if %STARTUP_ATTEMPT% geq %STARTUP_MAX_ATTEMPTS% GOTO healthcheck_loop_exit

SET /A STARTUP_ATTEMPT=%STARTUP_ATTEMPT%+1

echo "waiting for service (Healthcheck Attempt %STARTUP_ATTEMPT% of %STARTUP_MAX_ATTEMPTS%)"
timeout /t %STARTUP_WAIT_TIME%

:: Open the application in browser if the health-check passes
curl -s -o nul http://localhost:8080/health-check
if %ERRORLEVEL% equ 0 (
    explorer http://localhost:8080
    EXIT 0
)
GOTO healthcheck_loop
:healthcheck_loop_exit

echo "Startup Failed... Check your docker instance."