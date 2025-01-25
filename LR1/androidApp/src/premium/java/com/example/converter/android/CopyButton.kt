package com.example.converter.android

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun CopyButton(value: String) {
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = LocalSnackbarHostState.current
    val coroutineScope = rememberCoroutineScope()
    val onCopy: () -> Unit = {
        if(value.isNotEmpty()) {
            clipboardManager.setText(AnnotatedString(value))
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Copied: $value")
            }
        }
    }
    IconButton(onClick = onCopy) {
        Icon(
            Icons.Filled.ContentCopy,
            contentDescription = null,
            tint = Color.DarkGray,
            modifier = Modifier.size(32.dp))
    }
}