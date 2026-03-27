# Используем официальный образ Gradle для сборки
FROM gradle:8.5-jdk21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы проекта
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle gradle
COPY src src

# Собираем fat JAR
RUN gradle clean build --no-daemon

# Второй этап: запуск приложения (используем Eclipse Temurin)
FROM eclipse-temurin:21-jre-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR из первого этапа
COPY --from=builder /app/build/libs/drink-app-all.jar /app/app.jar

# Открываем порт
EXPOSE 8080

# Запускаем приложение
CMD ["java", "-jar", "app.jar"]