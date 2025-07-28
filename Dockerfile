# Stage 1: build con Maven + JDK 17
FROM maven:3.9.3-jdk-17 AS builder
WORKDIR /app

# Copia tutto il progetto dentro il container
COPY . .

# Builda il JAR saltando i test per velocizzare
RUN mvn clean package -DskipTests

# Stage 2: runtime con JDK 17 leggero
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copia il JAR buildato dallo stage precedente
COPY --from=builder /app/target/*.jar app.jar

# Espone la porta 8080 (standard Spring Boot)
EXPOSE 8080

# Comando per avviare la tua app con eventuali opzioni Java da env var
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
