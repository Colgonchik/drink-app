import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
    id("io.ktor.plugin") version "2.3.12" // Специальный плагин Ktor (рекомендуется)
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "com.drinkapp"
version = "1.0.0"

application {
    mainClass.set("com.drinkapp.ApplicationKt")
}

repositories {
    mavenCentral()
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

kotlin {
    jvmToolchain(21)
}

tasks {
    shadowJar {
        archiveClassifier.set("all")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        mergeServiceFiles() // Критически важно для Ktor
    }
}
