package com.example.tabatatimer.android.models

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabatatimer.android.services.Phase
import com.example.tabatatimer.android.services.TimerService
import com.example.tabatatimer.android.services.TimerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach



class TimerViewModel : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private var service: TimerService? = null
    private var serviceBound = false
    private var onServiceConnectedCallback: (() -> Unit)? = null

    private val _state = MutableStateFlow(TimerState())
    val uiState : StateFlow<TimerState> = _state

    val workCircles: Int
        get() {
            return this.service?.workCircles ?: 1
        }

    val totalRepeats: Int
        get() {
            return this.service?.totalRepeats ?: 1
        }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            println("✅ Service connected!")
            service = (binder as? TimerService.TimerBinder)?.getService()
            serviceBound = true
            service?.timerState?.onEach { _state.value = it }?.launchIn(viewModelScope)

            onServiceConnectedCallback?.invoke()
            onServiceConnectedCallback = null
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            println("❌ Service disconnected!")
            serviceBound = false
        }
    }

    fun bindService(context: Context, callback: () -> Unit) {
        onServiceConnectedCallback = callback
        val intent = Intent(context, TimerService::class.java)
        context.startForegroundService(intent)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun unbindService(context: Context) {
        if (serviceBound) {
            println("🔄 Unbinding service")
            context.unbindService(serviceConnection)
            serviceBound = false
        }
    }

    fun setPhases(phases: List<Phase>, workCircles: Int, totalRepeats: Int) {
        service?.setPhases(phases, workCircles, totalRepeats)
    }

    fun startTimer() {
        service?.startTimer()
    }

    fun pauseTimer() {
        service?.pauseTimer()
    }

    fun resetTimer() {
        service?.resetTimer()
    }

    fun moveToNextPhase() {
        service?.moveToNextPhase()
    }

    fun moveToPrevPhase() {
        service?.moveToPrevPhase()
    }
}

