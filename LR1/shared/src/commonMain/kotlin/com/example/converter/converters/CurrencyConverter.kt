package com.example.converter.converters

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyConverter(private val client: CurrencyClient) {
    private var exchangeRates = mutableMapOf<String, Map<String, Double>>()

    suspend fun loadRates(currencies: List<String>): String? {
        exchangeRates = mutableMapOf()
        var errorMessage: String? = null
        withContext(Dispatchers.IO) {
            try {
                for(currency in currencies) {
                    val response = client.getExchangeRates(currency.lowercase())
                        ?: throw Exception("Error fetching the rates!")
                    exchangeRates[currency.lowercase()] =
                        response.filterKeys { it.uppercase() in currencies && it != currency.lowercase() }
                }

                if (exchangeRates.isEmpty()) throw Exception("Error fetching the rates!")
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
        return errorMessage
    }

    fun convert(amount: Double, fromCurrency: String, toCurrency: String): Double {
        return if(fromCurrency == toCurrency) amount
            else (exchangeRates[fromCurrency.lowercase()]?.get(toCurrency.lowercase())?.let { rate ->
            amount * rate
        } ?: 0.0)
    }
}