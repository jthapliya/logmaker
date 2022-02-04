FROM openjdk:8-jdk-alpine
ARG APP_JAR
ENV JAVA_OPTS=""
ENV STRESS_VERSION=1.0.4

COPY ${APP_JAR} /usr/local/application.jar
COPY stress-$STRESS_VERSION.tar.gz .

RUN apk add --update bash g++ make curl && \
  tar xzvf stress-$STRESS_VERSION.tar.gz && \
  rm stress-$STRESS_VERSION.tar.gz && \
  cd stress-${STRESS_VERSION} && \
  ./configure && make && make install && \
  apk del g++ make curl && \
  rm -rf /tmp/* /var/tmp/* /var/cache/apk/* /var/cache/distfiles/*

ENTRYPOINT exec java -jar /usr/local/application.jar
