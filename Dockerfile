# Stage 1: build con Maven + JDK 21
FROM maven:3.9.3-jdk-21 AS builder
WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

# Stage 2: runtime con Eclipse Temurin JDK 21
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
