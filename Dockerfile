# Сборка
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle gradle
COPY src src
RUN gradle clean build --no-daemon

# Запуск — используем правильный образ Temurin
FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=builder /app/build/libs/drink-app-all.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]