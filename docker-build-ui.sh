#!/usr/bin/env sh

docker build \
  -f ./docker/ui/Dockerfile \
  --build-arg BASE_BUILDER_IMAGE="homesense-dependencies:latest" \
  -t homesense-ui:latest \
  .