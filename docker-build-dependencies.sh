#!/usr/bin/env sh

docker build \
  -f ./Dockerfile.dependencies \
  --build-arg BASE_BUILDER_IMAGE="maven:3.9.9-eclipse-temurin-21-alpine" \
  -t homesense-dependencies:latest \
  .