import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    java
}

group = "com.drinkapp"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    val ktor_version = "2.3.12"

    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
}

application {
    // Убедись, что путь к классу совпадает с твоим пакетом!
    mainClass.set("com.drinkapp.ApplicationKt")
}

tasks {
    // Синхронизируем Java и Kotlin под JVM 21
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "21"
        }
    }

    // Настройка ShadowJar (оставляем как было)
    shadowJar {
        archiveClassifier.set("all")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        mergeServiceFiles()
        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
    }
}

// Отключаем обычный jar, чтобы не путаться (опционально)
tasks.jar {
    enabled = false
}

// Привязываем сборку shadow к обычному build
tasks.build {
    dependsOn(tasks.shadowJar)
}
