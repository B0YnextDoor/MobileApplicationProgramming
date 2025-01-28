package com.example.tabatatimer.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BackButton(onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(50.dp)) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Turn Back",
            tint = Color.White,
            modifier = Modifier.fillMaxSize().clip(CircleShape).padding(6.dp)
                .background(Color.Black, CircleShape))
    }
}