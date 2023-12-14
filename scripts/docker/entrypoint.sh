#!/bin/bash

pushd /database || exit 1;
/liquibase/liquibase \
  --changelog-file "org/ivcode/homeportal/changelog/db.changelog-root.yml" \
  --url $DATABASE_URL \
  --username $DATABASE_USERNAME \
  --password $DATABASE_PASSWORD \
  update || exit 1;
popd || exit 1;

java -D \
 -Dspring.servlet.multipart.max-file-size=100MB \
 -Dspring.servlet.multipart.max-request-size=100MB \
 -Dspring.datasource.url=$DATABASE_URL \
 -Dspring.datasource.username=$DATABASE_USERNAME \
 -Dspring.datasource.password=$DATABASE_PASSWORD \
 -jar home-portal.jar