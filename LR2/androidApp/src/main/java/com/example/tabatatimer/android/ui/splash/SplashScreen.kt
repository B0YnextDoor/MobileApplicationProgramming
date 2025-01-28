package com.example.tabatatimer.android.ui.splash

import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tabatatimer.android.models.SplashScreenViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.example.tabatatimer.android.R

@Composable
fun SplashScreen(navigateToHome: () -> Unit, viewModel: SplashScreenViewModel = viewModel()) {
    val customFont = FontFamily(
        Font(R.font.sarina_font)
    )
    LaunchedEffect(Unit) {
        viewModel.startLoading { navigateToHome() }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)) {
            Text(
                text = "Tabata Timer",
                style = TextStyle(
                    fontFamily = customFont,
                    fontSize = 45.sp
                ),
                color = if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                    Color.White else Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            AndroidView(
                modifier = Modifier.height(300.dp)
                    .fillMaxWidth(),
                factory = { context ->
                    ImageView(context).apply {
                        Glide.with(context)
                            .asGif()
                            .load("file:///android_asset/startup.gif")
                            .into(this)
                    }
                }
            )
        }
    }
}