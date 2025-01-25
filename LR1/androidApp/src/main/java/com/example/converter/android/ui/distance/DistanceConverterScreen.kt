package com.example.converter.android.ui.distance

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
import com.example.converter.converters.distanceRates
import java.util.Locale

@Composable
fun DistanceConverterScreen() {
    val distances = listOf("cm", "pyad`", "vershok", "arshin", "sazhen`")

    var fromDistance by rememberSaveable { mutableStateOf(distances[0]) }
    var toDistance by rememberSaveable { mutableStateOf(distances[1]) }
    var amount by rememberSaveable { mutableStateOf("") }
    var convertedAmount by rememberSaveable { mutableStateOf("") }

    val updateConversion: (String) -> Unit = { newAmount ->
        amount = newAmount
        val amountDouble = amount.toDoubleOrNull() ?: 0.0
        convertedAmount = if(amountDouble > 0) String.format(
            Locale.getDefault(), "%.3f",
            convert(amountDouble, fromDistance, toDistance, distanceRates)
        ) else ""
    }

    val switchValues: () -> Unit = {
        val temp = fromDistance
        fromDistance = toDistance
        toDistance = temp
        updateConversion(convertedAmount)
    }

    val onFromDistanceChange: (String) -> Unit = { newDistance ->
        fromDistance = newDistance
        updateConversion(amount)
    }

    val onToDistanceChange: (String) -> Unit = { newDistance ->
        toDistance = newDistance
        updateConversion(amount)
    }

    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    if(isPortrait) {
        PortraitLayout(fromDistance, toDistance, amount, convertedAmount,
            distances, updateConversion, onFromDistanceChange, onToDistanceChange, switchValues)
    }
    else {
        LandscapeLayout(fromDistance, toDistance, amount, convertedAmount,
            distances, updateConversion, onFromDistanceChange, onToDistanceChange, switchValues)
    }
}

@Preview
@Composable
fun PreviewDistanceConverter() {
    DistanceConverterScreen()
}
