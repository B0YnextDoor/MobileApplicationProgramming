package com.example.converter.android.ui.currency

import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.converter.android.R
import com.example.converter.android.utils.NetworkUtils
import com.example.converter.converters.CurrencyClient
import com.example.converter.converters.CurrencyConverter
import java.util.Locale

@Composable
fun CurrencyConverterScreen() {
    val context = LocalContext.current
    val converter = remember { CurrencyConverter(CurrencyClient()) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    val currencies = listOf("RUB", "BYN", "USD", "EUR", "GBP")
    val currencyFlags = mapOf(
        "RUB" to R.drawable.rus,
        "BYN" to R.drawable.rb,
        "USD" to R.drawable.usa,
        "EUR" to R.drawable.eu,
        "GBP" to R.drawable.uk
    )

    var fromCurrency by rememberSaveable { mutableStateOf(currencies[0]) }
    var toCurrency by rememberSaveable { mutableStateOf(currencies[1]) }
    var amount by rememberSaveable { mutableStateOf("") }
    var convertedAmount by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        errorMessage = if (!NetworkUtils.isInternetAvailable(context)) {
            "No Internet connection!"
        } else {
            converter.loadRates(currencies)
        }
    }

    val updateConversion: (String) -> Unit = { newAmount ->
        if(errorMessage == null) {
            amount = newAmount
            val amountDouble = amount.toDoubleOrNull() ?: 0.0
            convertedAmount = if(amountDouble > 0) String.format(Locale.getDefault(), "%.3f",
                converter.convert(amountDouble, fromCurrency, toCurrency))
                else ""
        }
    }

    val switchValues: () -> Unit = {
        val temp = fromCurrency
        fromCurrency = toCurrency
        toCurrency = temp
        updateConversion(convertedAmount)
    }

    val onFromCurrencyChange: (String) -> Unit = { newCurrency ->
        fromCurrency = newCurrency
        updateConversion(amount)
    }

    val onToCurrencyChange: (String) -> Unit = { newCurrency ->
        toCurrency = newCurrency
        updateConversion(amount)
    }

    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    if(isPortrait) {
        PortraitLayout(
            fromCurrency, toCurrency, amount,
            convertedAmount, currencyFlags, updateConversion,
            onFromCurrencyChange, onToCurrencyChange, switchValues, errorMessage
        )
    }
    else {
        LandscapeLayout(
            fromCurrency, toCurrency, amount,
            convertedAmount, currencyFlags, updateConversion,
            onFromCurrencyChange, onToCurrencyChange, switchValues, errorMessage
        )
    }
}

@Preview
@Composable
fun PreviewCurrencyConverter() {
    CurrencyConverterScreen()
}
