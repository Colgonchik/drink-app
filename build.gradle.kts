import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
    java
    id("application")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

group = "com.drinkapp"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    val ktor_version = "2.3.12"

    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")

    // Этот модуль нужен для CallLogging (иногда его забывают)
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")

    // Эти два модуля нужны для ContentNegotiation и Jackson
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")

    // Логирование (Logback) обязательно должно быть, иначе сервер упадет при старте
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Для работы static и files в 2.x
    implementation("io.ktor:ktor-server-host-common:$ktor_version")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
}

application {
    mainClass.set("com.drinkapp.ApplicationKt")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.drinkapp.ApplicationKt"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    // Чтобы задача jar не падала при создании, используем lazy configuration
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
