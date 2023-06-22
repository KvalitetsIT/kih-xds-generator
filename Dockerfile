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

RUN ./gradlew -Pversion=$version build && ls -al build/libs/

FROM builder as test
CMD ["./gradlew", "test", "--info"]

FROM alpine:latest

ARG S6_OVERLAY_VERSION=3.1.5.0

ARG version=1.0.0
ENV ENV=production \
    TZ=Europe/Copenhagen \
    VERSION=$version \
    TIMEZONE=$TZ \
    LANG=C.UTF-8

RUN mkdir /app

COPY --from=builder /app/build/libs/xds-generator-$VERSION.jar /app/xds-generator.jar

RUN apk add --no-cache curl openjdk11-jre-headless ca-certificates nss && \
    echo "Build complete"

EXPOSE 9010
ENTRYPOINT exec java -jar /app/xds-generator.jar
