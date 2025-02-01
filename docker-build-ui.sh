#!/usr/bin/env sh

docker build \
  -f ./Dockerfile.ui \
  --build-arg BASE_BUILDER_IMAGE="homesense-dependencies:latest" \
  -t homesense-ui:latest \
  .