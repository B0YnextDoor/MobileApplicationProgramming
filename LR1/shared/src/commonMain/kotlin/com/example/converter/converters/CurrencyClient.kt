package com.example.converter.converters

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun jsonObjectToDoubleMap(jsonObject: JsonObject): Map<String, Double> {
    return jsonObject.mapValues { it.value.jsonPrimitive.doubleOrNull ?: 0.0 }
}

class CurrencyClient {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val API_URL = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/"

    suspend fun getExchangeRates(currency: String): Map<String, Double>? {
        return try {
            val response: HttpResponse = client.get("${API_URL}${currency}.json")
            if (response.status == HttpStatusCode.OK) {
                val responseBody = response.body<String>()
                val jsonObject = Json.parseToJsonElement(responseBody).jsonObject
                jsonObject[currency]?.jsonObject?.let { jsonObjectToDoubleMap(it) }
            }
            else null
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            null
        }
    }
}