package com.example.converter.android.ui.currency

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.converter.android.R
import com.example.converter.android.SwapButton


@Composable
fun CurrencySelection(
    fromCurrency: String,
    toCurrency: String,
    currencyFlags: Map<String, Int>,
    onFromCurrencyChange: (String) -> Unit,
    onToCurrencyChange: (String) -> Unit,
    switchValues: () -> Unit,
    isPremium: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CurrencyDropdown(
            selectedCurrency = fromCurrency,
            currencyFlags = currencyFlags.filterNot { it.key == toCurrency },
            onCurrencySelected = onFromCurrencyChange
        )
        if(isPremium)
            SwapButton(switchValues)
        CurrencyDropdown(
            selectedCurrency = toCurrency,
            currencyFlags = currencyFlags.filterNot { it.key == fromCurrency },
            onCurrencySelected = onToCurrencyChange
        )
    }
}

@Composable
fun CurrencyDropdown(
    selectedCurrency: String,
    currencyFlags: Map<String, Int>,
    onCurrencySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .height(60.dp)
                .width(140.dp)
                .background(Color.DarkGray, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp)
        ) {
            CurrencyItem(selectedCurrency, currencyFlags)
        }

        DropdownMenu(
            expanded = expanded,
            modifier = Modifier
                .background(Color.DarkGray),
            onDismissRequest = { expanded = false }
        ) {
            currencyFlags.keys.forEach { currency ->
                DropdownMenuItem(
                    text = { CurrencyItem(currency, currencyFlags) },
                    onClick = {
                        onCurrencySelected(currency)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CurrencyItem(currency: String,
                 currencyFlags: Map<String, Int>) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = currencyFlags[currency] ?: R.drawable.rb),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = currency,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
