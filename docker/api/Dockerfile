ARG BASE_BUILDER_IMAGE="maven:3.9.9-eclipse-temurin-21-alpine"
FROM ${BASE_BUILDER_IMAGE} AS builder
WORKDIR /root/src
COPY . .
RUN mvn package

FROM eclipse-temurin:21-alpine

WORKDIR /opt/gr/ihu/robotics/homesense
COPY --from=builder /root/src/homesense-api/target/homesense-api-1.0-SNAPSHOT.jar ./homesense-api.jar

ENV LC_ALL="en_US.UTF-8" \
    LANG="en_US.UTF-8" \
    TZ="Europe/Athens" \
    ACTIVE_PROFILE="production" \
    SERVER_PORT="8080" \
    DATASOURCE_URL="jdbc:postgresql://localhost:5432/homesense" \
    DATASOURCE_USERNAME="homesense" \
    HOMESENSE_ALLOWED_IPS="127.0.0.1, 0:0:0:0:0:0:0:1" \
    NODESENSE_IP_PREFIX="192.168.1" \
    NODESENSE_TIMEOUT="500" \
    NODESENSE_SCHEDULED_TIMEOUT="1000" \
    NODESENSE_SCHEDULED_INTERVAL="5000" \
    NODESENSE_SCHEDULED_DELAY="1000"

ENTRYPOINT ["java", "-jar", "/opt/gr/ihu/robotics/homesense/homesense-api.jar", "--spring.profiles.active=${ACTIVE_PROFILE}"]