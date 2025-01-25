package com.example.converter.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Dropdown(
    selected: String,
    valuesNames: List<String>,
    onSelect: (String) -> Unit
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
                Text(
                    text = selected.uppercase(),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        DropdownMenu(
            expanded = expanded,
            modifier = Modifier
                .background(Color.DarkGray),
            onDismissRequest = { expanded = false }
        ) {
            valuesNames.forEach { value ->
                DropdownMenuItem(
                    text = { Text(text = value.uppercase(), fontSize = 18.sp, color = Color.White) },
                    onClick = {
                        onSelect(value)
                        expanded = false
                    }
                )

            }
        }
    }
}