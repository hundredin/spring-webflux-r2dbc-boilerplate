#!/bin/bash

docker run --name postgresql-spring \
  -p 5432:5432 \
  -e POSTGRES_DB=springdb \
  -e POSTGRES_USER=spring \
  -e POSTGRES_PASSWORD=spring0! \
  -e LANG=ko_KR.utf8 -e POSTGRES_INITDB_ARGS="--locale-provider=icu --icu-locale=ko-KR" \
  -v ~/docker-volumes/spring/postgresql:/var/lib/postgresql \
  -d arm64v8/postgres:15-alpine
