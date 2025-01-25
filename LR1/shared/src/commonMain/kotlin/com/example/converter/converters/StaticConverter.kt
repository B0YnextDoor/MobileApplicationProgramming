package com.example.converter.converters

val weightRates = mapOf(
    "gram" to mapOf("ounce" to 0.03527, "pound" to 0.0022, "kg" to 0.001),
    "ounce" to mapOf("gram" to 28.3495, "pound" to 0.0625, "kg" to 0.0283495),
    "pound" to mapOf("gram" to 453.59, "ounce" to 16.0, "kg" to 0.45359),
    "kg" to mapOf("gram" to 1000.0, "ounce" to 35.27396, "pound" to 2.2046)
)

val distanceRates = mapOf(
    "cm" to mapOf("pyad`" to 0.05624, "vershok" to 0.22727, "arshin" to 0.014061,
        "sazhen`" to 0.0046869),
    "pyad`" to mapOf("cm" to 17.78, "vershok" to 4.04091, "arshin" to 0.25,
        "sazhen`" to 0.08333),
    "vershok" to mapOf("cm" to 4.4, "pyad`" to 0.247469, "arshin" to 0.061867,
        "sazhen`" to 0.02062),
    "arshin" to mapOf("cm" to 71.12, "pyad`" to 4.0, "vershok" to 16.16364,
        "sazhen`" to 0.3333),
    "sazhen`" to mapOf("cm" to 213.36, "pyad`" to 12.0, "vershok" to 48.49091, "arshin" to 3.0)
)

fun convert(amount: Double, from: String, to: String,
            rates: Map<String, Map<String, Double>>): Double {
    return if (from.lowercase() == to.lowercase()) amount
    else (rates[from.lowercase()]?.get(to.lowercase())?.let { rate ->
        amount * rate
    } ?: 0.0)
}