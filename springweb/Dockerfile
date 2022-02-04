FROM openjdk:8-jdk-alpine
ARG APP_JAR
ENV JAVA_OPTS=""
COPY ${APP_JAR} /usr/local/application.jar
ENTRYPOINT exec java -jar /usr/local/application.jar
