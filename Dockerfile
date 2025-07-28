FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/ZipZap-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
