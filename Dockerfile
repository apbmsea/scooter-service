FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Используйте один из этих вариантов вместо openjdk:17-jdk-slim:
FROM openjdk:17-slim
# ИЛИ
FROM openjdk:17-jre-slim
# ИЛИ
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/target/User-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--server.address=0.0.0.0"]