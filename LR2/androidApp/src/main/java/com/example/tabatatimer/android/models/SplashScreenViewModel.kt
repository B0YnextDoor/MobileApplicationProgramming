package com.example.tabatatimer.android.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel() {
    fun startLoading(onFinished: () -> Unit) {
        viewModelScope.launch {
            delay(3000)
            onFinished()
        }
    }
}