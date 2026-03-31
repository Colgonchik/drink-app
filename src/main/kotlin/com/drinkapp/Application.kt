package com.drinkapp

import DrinkStorage
import io.ktor.serialization.jackson.* // Важно для jackson {}
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
//import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.* // НУЖЕН ДЛЯ static и files
import io.ktor.server.plugins.callloging.CallLogging
import org.slf4j.event.Level
import java.io.File
import com.drinkapp.LoginRequest
import io.ktor.http.HttpStatusCode


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    // Создаем хранилище и загружаем рецепты
    val storage = DrinkStorage()
    storage.loadFromJson("drinks.json")

    println("🍹 Бар запущен! Загружено рецептов: ${storage.getCount()}")

    // Настройка сериализации JSON
    install(ContentNegotiation) {
        jackson {
            // настройки Jackson
        }
    }

    // Логирование запросов
    install(CallLogging) {
        level = Level.INFO
        format { call ->
            "${call.request.httpMethod.value} ${call.request.uri}"
        }
    }

    // Статические файлы (HTML, CSS)
    routing {
        // Вместо install(StaticContent) используйте это внутри routing:
        static("/static") {
            files("src/main/resources/static")
        }

        // Остальные ваши get("/") и т.д.
    }

    // API эндпоинты
    routing {
        // Главная страница
        get("/") {
            call.respondFile(File("src/main/resources/static/index.html"))
        }

        // Вход для сотрудников (простая проверка пароля)
        post("/api/login") {
            val staffPassword = System.getenv("STAFF_PASSWORD")
            val request = call.receive<LoginRequest>()

            if (request.password == staffPassword) {
                call.respond(mapOf("token" to "staff-ok"))
            } else {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Неверный пароль"))
            }
        }

// Защищённые раскладки
        get("/api/recipes") {
            val auth = call.request.headers["Authorization"]
            if (auth?.contains("staff-ok") != true) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Требуется авторизация"))
                return@get
            }

            // Возвращаем все рецепты из вашего хранилища
            val recipes = storage.getAllDrinks().map { drink ->
                mapOf(
                    "name" to drink.name,
                    "ingredients" to drink.ingredients,
                    "method" to drink.method,
                    "glass" to drink.glass,
                    "garnish" to drink.garnish,
                    "measurements" to drink.measurements
                )
            }
            call.respond(recipes)
        }

        // API: случайный напиток
        get("/api/random") {
            val drink = storage.getRandomDrink()
            if (drink != null) {
                call.respond(mapOf(
                    "name" to drink.name,
                    "ingredients" to drink.ingredients,
                    "method" to drink.method,
                    "glass" to drink.glass,
                    "garnish" to drink.garnish
                ))
            } else {
                call.respond(mapOf("error" to "Нет рецептов"))
            }
        }

        // API: все напитки
        get("/api/all") {
            val drinks = storage.getAllDrinks().map { drink ->
                mapOf(
                    "name" to drink.name,
                    "ingredients" to drink.ingredients,
                    "method" to drink.method,
                    "glass" to drink.glass,
                    "garnish" to drink.garnish
                )
            }
            call.respond(drinks)
        }

        // API: поиск по ингредиенту
        get("/api/search") {
            val ingredient = call.request.queryParameters["ingredient"]
            if (ingredient.isNullOrBlank()) {
                call.respond(mapOf("error" to "Укажите ингредиент"))
                return@get
            }

            val found = storage.getDrinksByIngredient(ingredient)
            val result = found.map { drink ->
                mapOf(
                    "name" to drink.name,
                    "ingredients" to drink.ingredients,
                    "method" to drink.method,
                    "glass" to drink.glass,
                    "garnish" to drink.garnish
                )
            }
            call.respond(result)
        }

        // API: статистика
        get("/api/stats") {
            call.respond(mapOf(
                "total" to storage.getCount(),
                "message" to "Бар готов к работе! 🍹"
            ))
        }
    }
}