#!/usr/bin/env sh

docker build \
  -f ./docker/api/Dockerfile \
  --build-arg BASE_BUILDER_IMAGE="homesense-dependencies:latest" \
  -t homesense-api:latest \
  .