FROM oracle/graalvm-ce:20.1.0-java11 as graalvm
RUN gu install native-image

COPY . /home/app/cache-guide
WORKDIR /home/app/cache-guide

RUN native-image --no-server -cp build/libs/cache-guide-*-all.jar

FROM frolvlad/alpine-glibc
RUN apk update && apk add libstdc++
EXPOSE 8080
COPY --from=graalvm /home/app/cache-guide/cache-guide /app/cache-guide
ENTRYPOINT ["/app/cache-guide"]