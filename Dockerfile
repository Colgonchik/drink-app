# 1. Сборка
FROM gradle:8.5-jdk21 AS builder

# Ограничиваем аппетит Gradle (Render на бесплатных тарифах дает мало RAM)
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.jvmargs='-Xmx512m'"

WORKDIR /app

# Копируем всё сразу (так надежнее, если структура папок сложная)
COPY . .

# Даем права и собираем именно shadowJar
RUN chmod +x gradlew && ./gradlew clean shadowJar --no-daemon

# 2. Запуск
FROM eclipse-temurin:21-jre

WORKDIR /app

# Копируем только собранный JAR из первой стадии
# Используем маску *-all.jar, так как shadowJar создает именно такой файл
COPY --from=builder /app/build/libs/*-all.jar app.jar

EXPOSE 8080

# Запускаем через ENTRYPOINT, это надежнее для прокидывания сигналов остановки
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]
