package com.example.converter.android.ui.weight

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.example.converter.android.ui.layout.LandscapeLayout
import com.example.converter.android.ui.layout.PortraitLayout
import com.example.converter.converters.convert
import com.example.converter.converters.weightRates
import java.util.Locale

@Composable
fun WeightConverterScreen() {
    val weights = listOf("gram", "ounce", "pound", "kg")

    var fromWeight by rememberSaveable { mutableStateOf(weights[0]) }
    var toWeight by rememberSaveable { mutableStateOf(weights[1]) }
    var amount by rememberSaveable { mutableStateOf("") }
    var convertedAmount by rememberSaveable { mutableStateOf("") }

    val updateConversion: (String) -> Unit = { newAmount ->
        amount = newAmount
        val amountDouble = amount.toDoubleOrNull() ?: 0.0
        convertedAmount = if(amountDouble > 0) String.format(Locale.getDefault(), "%.3f",
            convert(amountDouble, fromWeight, toWeight, weightRates)) else ""
    }

    val switchValues: () -> Unit = {
        val temp = fromWeight
        fromWeight = toWeight
        toWeight = temp
        updateConversion(convertedAmount)
    }

    val onFromWeightChange: (String) -> Unit = { newWeight ->
        fromWeight = newWeight
        updateConversion(amount)
    }

    val onToWeightChange: (String) -> Unit = { newWeight ->
        toWeight = newWeight
        updateConversion(amount)
    }

    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    if(isPortrait) {
        PortraitLayout(fromWeight, toWeight, amount, convertedAmount,
            weights, updateConversion, onFromWeightChange, onToWeightChange, switchValues)
    }
    else {
        LandscapeLayout(fromWeight, toWeight, amount, convertedAmount,
            weights, updateConversion, onFromWeightChange, onToWeightChange, switchValues)
    }
}

@Preview
@Composable
fun PreviewWeightConverter() {
   WeightConverterScreen()
}
