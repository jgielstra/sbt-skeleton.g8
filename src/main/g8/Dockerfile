FROM openjdk:8-jre-alpine
MAINTAINER $organization$

ADD target/universal/stage /app

RUN apk --no-cache add bash

EXPOSE 8080

CMD "/app/bin/$name;format="lower,word"$"
