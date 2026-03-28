FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
# Копируем ВООБЩЕ ВСЁ, чтобы точно ничего не забыть
COPY . .
# Даем права (на всякий случай) и собираем
RUN ./gradlew clean shadowJar --no-daemon


FROM eclipse-temurin:21-jre
WORKDIR /app
# Используем маску *.jar, чтобы не гадать с именем и версией
COPY --from=builder /app/build/libs/*-all.jar app.jar


EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
