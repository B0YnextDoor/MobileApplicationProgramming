package com.example.converter.android.models

data class ConverterState(
    val from: String,
    val to: String,
    val values: List<String>,
    val amount: String = "",
    val convertedAmount: String = "",
)
