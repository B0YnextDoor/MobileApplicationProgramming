package com.example.converter.android.ui.currency

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.converter.android.BuildConfig
import com.example.converter.android.CopyButton
import com.example.converter.android.components.Field
import com.example.converter.android.components.NumericKeypad

@Composable
fun PortraitLayout(
    fromCurrency: String,
    toCurrency: String,
    amount: String,
    convertedAmount: String,
    currencyFlags: Map<String, Int>,
    updateConversion: (String) -> Unit,
    onFromCurrencyChange: (String) -> Unit,
    onToCurrencyChange: (String) -> Unit,
    switchValues: () -> Unit,
    errorMessage: String?
) {
    val isPremium = BuildConfig.FLAVOR == "premium"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        errorMessage?.let {
            Text(text = it, color = Color.Red, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
        }
        CurrencySelection(fromCurrency, toCurrency, currencyFlags,
            onFromCurrencyChange, onToCurrencyChange, switchValues, isPremium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            if(isPremium) {
                CopyButton(amount)
                Spacer(Modifier.width(8.dp))
            }
            Field(amount, updateConversion)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            if(isPremium) {
                CopyButton(convertedAmount)
                Spacer(Modifier.width(8.dp))
            }
            Field(convertedAmount, isInput = false)
        }
        NumericKeypad(amount, updateConversion)
    }
}