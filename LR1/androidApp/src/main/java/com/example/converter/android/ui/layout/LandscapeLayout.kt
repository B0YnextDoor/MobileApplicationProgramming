package com.example.converter.android.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
fun LandscapeLayout(
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
    Row(modifier = Modifier.fillMaxSize().offset(y=30.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(10.dp)) {
            item {
                Column(modifier = Modifier.fillMaxHeight().offset(y=25.dp)) {
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
                }
            }
            item {
                NumericKeypad(amount, updateConversion)
            }
        }
    }
}