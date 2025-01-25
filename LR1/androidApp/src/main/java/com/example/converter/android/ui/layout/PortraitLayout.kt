package com.example.converter.android.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.converter.android.BuildConfig
import com.example.converter.android.CopyButton
import com.example.converter.android.components.Field
import com.example.converter.android.components.NumericKeypad
import com.example.converter.android.components.SelectionRow

@Composable
fun PortraitLayout(
    from: String,
    to: String,
    amount: String,
    convertedAmount: String,
    values: List<String>,
    updateConversion: (String) -> Unit,
    onFromChange: (String) -> Unit,
    onToChange: (String) -> Unit,
    switchValues: () -> Unit
) {
    val isPremium = BuildConfig.FLAVOR == "premium"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        SelectionRow(from, to, values, onFromChange, onToChange, switchValues, isPremium)
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