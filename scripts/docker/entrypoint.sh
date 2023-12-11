#!/bin/bash

java -D \
 -Dspring.servlet.multipart.max-file-size=100MB \
 -Dspring.servlet.multipart.max-request-size=100MB \
 -Dspring.datasource.url=$DATABASE_URL \
 -Dspring.datasource.username=$DATABASE_USERNAME \
 -Dspring.datasource.password=$DATABASE_PASSWORD \
 -jar home-portal.jar