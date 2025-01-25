package com.example.converter.android.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.converter.android.R
import com.example.converter.converters.CurrencyClient
import com.example.converter.converters.CurrencyConverter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

val currencies = listOf("RUB", "BYN", "USD", "EUR", "GBP")

data class CurrencyConverterUiState (
    val currencyFlags: Map<String, Int> = mapOf(
        "RUB" to R.drawable.rus,
        "BYN" to R.drawable.rb,
        "USD" to R.drawable.usa,
        "EUR" to R.drawable.eu,
        "GBP" to R.drawable.uk
    ),
    val converterState: ConverterState = ConverterState(from = currencies[0],
        to = currencies[1], values = currencies),
    val errorMessage: String? = null
)

class CurrencyConverterViewModel : ViewModel() {
    private val converter: CurrencyConverter = CurrencyConverter(CurrencyClient())
    private val _uiState = MutableStateFlow(CurrencyConverterUiState())
    val uiState: StateFlow<CurrencyConverterUiState> = _uiState

    fun loadCurrencyRates(isInternetAvailable: Boolean) {
        viewModelScope.launch {
            val errorMessage = if (!isInternetAvailable) {
                "No Internet connection!"
            } else {
                converter.loadRates(_uiState.value.currencyFlags.keys.toList())
            }
            _uiState.value = _uiState.value.copy(errorMessage = errorMessage)
        }
    }

    fun updateAmount(newAmount: String) {
        val currentState = _uiState.value
        val amountDouble = newAmount.toDoubleOrNull() ?: 0.0
        val convertedAmount = if (amountDouble > 0 && currentState.errorMessage == null) {
            String.format(
                Locale.getDefault(), "%.3f",
                converter.convert(amountDouble,
                    currentState.converterState.from,
                    currentState.converterState.to)
            )
        } else {
            ""
        }
        _uiState.value = currentState.copy(
            converterState = currentState.converterState.copy(
                amount = newAmount,
                convertedAmount = convertedAmount
            )
        )
    }

    fun switchCurrencies() {
        val currentState = _uiState.value
        val newState = currentState.converterState.copy(
            from = currentState.converterState.to,
            to = currentState.converterState.from,
            amount = currentState.converterState.convertedAmount,
            convertedAmount = ""
        )
        _uiState.value = currentState.copy(converterState = newState)
        updateAmount(newState.amount)
    }

    fun onFromCurrencyChanged(newCurrency: String) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            converterState = currentState.converterState.copy(from = newCurrency)
        )
        updateAmount(currentState.converterState.amount)
    }

    fun onToCurrencyChanged(newCurrency: String) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            converterState = currentState.converterState.copy(to = newCurrency)
        )
        updateAmount(currentState.converterState.amount)
    }
}