#!/usr/bin/env sh

docker build \
  -f ./Dockerfile.api \
  --build-arg BASE_BUILDER_IMAGE="homesense-dependencies:latest" \
  -t homesense-api:latest \
  .