package com.example.converter.android.models

import androidx.lifecycle.ViewModel
import com.example.converter.converters.convert
import com.example.converter.converters.weightRates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

val weights = listOf("gram", "ounce", "pound", "kg")

class WeightConverterViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ConverterState(from = weights[0],
        to = weights[1], values = weights))
    val uiState: StateFlow<ConverterState> = _uiState

    fun updateAmount(newAmount: String) {
        val currentState = _uiState.value
        val amountDouble = newAmount.toDoubleOrNull() ?: 0.0
        val convertedAmount = if (amountDouble > 0) {
            String.format(Locale.getDefault(), "%.3f",
                convert(amountDouble, currentState.from,
                    currentState.to, weightRates)
            )
        } else {
            ""
        }
        _uiState.value = currentState.copy(
            amount = newAmount,
            convertedAmount = convertedAmount
        )
    }

    fun switchWeights() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            from = currentState.to,
            to = currentState.from,
            amount = currentState.convertedAmount,
            convertedAmount = "")
        updateAmount(_uiState.value.amount)
    }

    fun onFromWeightChanged(newWeight: String) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            from = newWeight
        )
        updateAmount(currentState.amount)
    }

    fun onToWeightChanged(newWeight: String) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            to = newWeight
        )
        updateAmount(currentState.amount)
    }
}