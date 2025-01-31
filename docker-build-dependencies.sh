#!/usr/bin/env sh

docker build \
  -f ./docker/Dockerfile.dependencies \
  --build-arg BASE_BUILDER_IMAGE="maven:3-amazoncorretto-21" \
  -t homesense-dependencies:latest \
  .