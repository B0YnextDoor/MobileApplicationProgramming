package com.example.converter.android.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumericKeypad(
    amount: String,
    onClick: (String) -> Unit
) {
    val btns = (1..9).map {it.toString()} + listOf("0", ".", "C")

    val onDigitClick: (String) -> Unit = { btn ->
        if(isValidInput(amount)) onClick(amount + btn)
    }
    val onDotClick: () -> Unit = {
        if (amount.isNotEmpty() && !amount.contains("."))
            onClick("${amount}.")
    }
    val onClearClick: () -> Unit = { onClick("") }

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Spacer(modifier = if(isPortrait) Modifier.height(8.dp) else Modifier.width(8.dp))
    Box(
        modifier = Modifier
            .then(if (isPortrait) Modifier.fillMaxWidth() else Modifier.fillMaxHeight())
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth().then(if(!isPortrait) Modifier.height(500.dp) else Modifier)
        ) {
            items(btns.size) { index ->
                val btn = btns[index]
                KeypadButton(digit = btn, onClick = {
                    when {
                        btn.toIntOrNull() != null -> onDigitClick(btn)
                        btn == "." -> onDotClick()
                        else -> onClearClick()
                    }
                }, isPortrait)
            }
        }
    }
}

@Composable
fun KeypadButton(digit: String, onClick: () -> Unit, isPortrait: Boolean) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(6.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.DarkGray,
            contentColor = Color.White,
            disabledContentColor = Color.White,
            disabledContainerColor = Color.DarkGray
        )
    ) {
        Text(text = digit, fontSize = if(isPortrait) 50.sp else 30.sp, color = Color.White)
    }
}

fun isValidInput(input: String): Boolean {
    if (input.isEmpty()) return true

    val maxLength = 15
    if(!input.contains('.')) return input.length < maxLength

    val regex = """^\d+(\.\d{0,4})?$""".toRegex()
    return input.length <= maxLength && regex.matches(input)
}
