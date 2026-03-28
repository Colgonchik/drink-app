FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

# Копируем только файлы сборки сначала (для кэша)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY src src

# Даем права и запускаем сборку с МИНИМАЛЬНЫМ потреблением памяти
RUN chmod +x gradlew && \
    ./gradlew shadowJar --no-daemon -Dorg.gradle.jvmargs="-Xmx256m -XX:MaxMetaspaceSize=128m"

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*-all.jar app.jar

EXPOSE 8080
# Запуск тоже с лимитом, чтобы Render не прибил процесс
CMD ["java", "-Xmx300m", "-jar", "app.jar"]
