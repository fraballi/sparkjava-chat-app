FROM openjdk:11-jre-slim

LABEL maintainer = "Félix Roberto Aballí Morell"

ADD build/distributions/*.tar  /

USER root

RUN mv /java-boilerplate-1.0 /app && chmod 0644 -R /app

WORKDIR /app/bin

EXPOSE 8080

CMD ["sh",  "java-boilerplate", "-Djava.security.egd=file:/dev/./urandom"]