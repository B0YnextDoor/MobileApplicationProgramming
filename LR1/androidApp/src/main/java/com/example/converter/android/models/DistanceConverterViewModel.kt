package com.example.converter.android.models

import androidx.lifecycle.ViewModel
import com.example.converter.converters.convert
import com.example.converter.converters.distanceRates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

val distances = listOf("cm", "pyad`", "vershok", "arshin", "sazhen`")

class DistanceConverterViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ConverterState(from = distances[0],
        to = distances[1], values = distances))
    val uiState: StateFlow<ConverterState> = _uiState

    fun updateAmount(newAmount: String) {
        val currentState = _uiState.value
        val amountDouble = newAmount.toDoubleOrNull() ?: 0.0
        val convertedAmount = if (amountDouble > 0) {
            String.format(
                Locale.getDefault(), "%.3f",
                convert(amountDouble, currentState.from,
                    currentState.to, distanceRates
                )
            )
        } else {
            ""
        }
        _uiState.value = currentState.copy(
            amount = newAmount,
            convertedAmount = convertedAmount
        )
    }

    fun switchDistances() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            from = currentState.to,
            to = currentState.from,
            amount = currentState.convertedAmount,
            convertedAmount = "")
        updateAmount(_uiState.value.amount)
    }

    fun onFromDistanceChanged(newDistance: String) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            from = newDistance
        )
        updateAmount(currentState.amount)
    }

    fun onToDistanceChanged(newDistance: String) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            to = newDistance
        )
        updateAmount(currentState.amount)
    }
}