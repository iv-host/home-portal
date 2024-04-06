#!/bin/sh

PROFILE=" \
  -Dserver.forward-headers-strategy=FRAMEWORK \
  -Dspring.servlet.multipart.max-file-size=100MB \
  -Dspring.servlet.multipart.max-request-size=100MB \
  -Dspring.liquibase.enabled=true \
  -Dspring.liquibase.change-log=classpath:/database/db.changelog-root.yml \
"

[ ! -z "$DATABASE_URL" ] && PROFILE="$PROFILE -Dspring.datasource.url=$DATABASE_URL" || { echo "'DATABASE_URL' env variable is required"; exit 1; }
[ ! -z "$DATABASE_USERNAME" ] && PROFILE="$PROFILE -Dspring.datasource.username=$DATABASE_USERNAME"
[ ! -z "$DATABASE_PASSWORD" ] && PROFILE="$PROFILE -Dspring.datasource.password=$DATABASE_PASSWORD"

if [ "$(echo "$OAUTH2_ENABLED" | awk '{print tolower($0)}')" = "true" ]; then
  PROFILE="$PROFILE -Dsecurity.oauth2.enabled=true"

  [ ! -z "$OAUTH2_ADMIN" ] && PROFILE="$PROFILE -Dsecurity.oauth2.admin=$OAUTH2_ADMIN" || { echo "'OAUTH2_ADMIN' env variable is required if OAuth2 is enabled"; exit 1; }
  [ ! -z "$OAUTH2_ISSUER_URL" ] && PROFILE="$PROFILE -Dsecurity.oauth2.issuer-url=$OAUTH2_ISSUER_URL" || { echo "'OAUTH2_ISSUER_URL' env variable is required if OAuth2 is enabled"; exit 1; }
  [ ! -z "$OAUTH2_AUTH_URL" ] && PROFILE="$PROFILE -Dsecurity.oauth2.client.authorization-url=$OAUTH2_AUTH_URL" || { echo "'OAUTH2_AUTH_URL' env variable is required if OAuth2 is enabled"; exit 1; }
  [ ! -z "$OAUTH2_TOKEN_URL" ] && PROFILE="$PROFILE -Dsecurity.oauth2.client.token-url=$OAUTH2_TOKEN_URL" || { echo "'OAUTH2_TOKEN_URL' env variable is required if OAuth2 is enabled"; exit 1; }
  [ ! -z "$OAUTH2_CLIENT_ID" ] && PROFILE="$PROFILE -Dsecurity.oauth2.client.client-id=$OAUTH2_CLIENT_ID" || { echo "'OAUTH2_CLIENT_ID' env variable is required if OAuth2 is enabled"; exit 1; }
  [ ! -z "$OAUTH2_CLIENT_SECRET" ] && PROFILE="$PROFILE -Dsecurity.oauth2.client.client-secret=$OAUTH2_CLIENT_SECRET" || { echo "'OAUTH2_CLIENT_SECRET' env variable is required if OAuth2 is enabled"; exit 1; }
  [ ! -z "$OAUTH2_CLIENT_SCOPE" ] && PROFILE="$PROFILE -Dsecurity.oauth2.client.scope=$OAUTH2_CLIENT_SCOPE"
fi


java "$PROFILE" -jar home-portal.jar