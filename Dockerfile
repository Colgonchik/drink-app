# Сборка
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test --no-daemon


# Запуск
FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=builder /app/build/libs/drink-app-1.0.0.jar /app/app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]