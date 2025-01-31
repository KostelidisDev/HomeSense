ARG BASE_BUILDER_IMAGE="maven:3-amazoncorretto-21"
FROM ${BASE_BUILDER_IMAGE} AS builder
WORKDIR /root/src
COPY . .
RUN mvn package

FROM amazoncorretto:21

WORKDIR /opt/gr/ihu/robotics/homesense
COPY --from=builder /root/src/homesense-ui/target/homesense-ui-1.0-SNAPSHOT.jar ./homesense-ui.jar

ENV LC_ALL="en_US.UTF-8" \
    LANG="en_US.UTF-8" \
    TZ="Europe/Athens" \
    ACTIVE_PROFILE="production" \
    SERVER_PORT="4000"

ENTRYPOINT ["java", "-jar", "/opt/gr/ihu/robotics/homesense/homesense-ui.jar", "--spring.profiles.active=${ACTIVE_PROFILE}"]