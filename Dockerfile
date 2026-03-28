FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean shadowJar --no-daemon --stacktrace

FROM eclipse-temurin:21-jre
WORKDIR /app
# Плагин shadow положит файл в build/libs/
COPY --from=builder /app/build/libs/*-all.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
