package com.example.converter.android.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.converter.android.SwapButton

@Composable
fun SelectionRow(
    from: String,
    to: String,
    values: List<String>,
    onFromChange: (String) -> Unit,
    onToChange: (String) -> Unit,
    switchValues: () -> Unit,
    isPremium: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Dropdown(
            selected = from,
            valuesNames = values.filterNot { it == to },
            onSelect = onFromChange
        )
        if(isPremium)
            SwapButton(switchValues)
        Dropdown(
            selected = to,
            valuesNames = values.filterNot { it == from },
            onSelect = onToChange
        )
    }
}