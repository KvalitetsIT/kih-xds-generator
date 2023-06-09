FROM openjdk:11-jdk as builder

ARG version=1.0.0
ENV VERSION=$version
WORKDIR /app/

COPY build.gradle .
COPY gradle gradle
COPY gradlew .
COPY settings.gradle .
COPY src src
COPY lib lib

RUN ./gradlew -Pversion=$version build && ls -al build/libs/ && echo "Is it set? $AWS_ACCESS_KEY_ID"

FROM alpine:latest

MAINTAINER "OpenTeleHealth Tech Support <tech-support@opentelehealth.com>"
ARG S6_OVERLAY_VERSION=3.1.5.0
ARG version=1.0.0
ENV ENV=production \
    TZ=Europe/Copenhagen \
    VERSION=$version \
    TIMEZONE=$TZ \
    LANG=C.UTF-8

ADD https://github.com/just-containers/s6-overlay/releases/download/v${S6_OVERLAY_VERSION}/s6-overlay-noarch.tar.xz /tmp
RUN tar -C / -Jxpf /tmp/s6-overlay-noarch.tar.xz
ADD https://github.com/just-containers/s6-overlay/releases/download/v${S6_OVERLAY_VERSION}/s6-overlay-x86_64.tar.xz /tmp
RUN tar -C / -Jxpf /tmp/s6-overlay-x86_64.tar.xz

RUN mkdir /app

COPY --from=builder /app/build/libs/xds-generator-$VERSION.jar /app/xds-generator.jar

RUN apk add --no-cache curl openjdk11-jre-headless ca-certificates nss && \
    echo "Build complete"

ADD docker/root /

EXPOSE 9010
ENTRYPOINT ["/init"]
CMD []
