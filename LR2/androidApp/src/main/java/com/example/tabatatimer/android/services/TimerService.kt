package com.example.tabatatimer.android.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.tabatatimer.android.R
import com.example.tabatatimer.android.domain.PHASE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Phase(
    val name: String,
    val duration: Int
)

data class TimerState(
    val currentCircle : Int = 1,
    val currentPhase: Int = 1,
    val currentPhaseIndex: Int = 0,
    val phases: List<Phase> = emptyList(),
    val remainingTime: Int = 0,
    val isRunning: Boolean = false
)

class TimerService: Service() {
    private val binder = TimerBinder()
    private var timerJob: Job? = null

    private val _state = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _state

    var workCircles: Int = 0
    var totalRepeats: Int = 0

    private var mediaPlayer: MediaPlayer? = null

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    private fun createNotification(content: String): Notification {
        val channelId = "timer_channel"
        val channel = NotificationChannel(channelId, "Timer", NotificationManager.IMPORTANCE_LOW)
        val manager = getSystemService(NotificationManager::class.java)

        val existingChannel = manager.getNotificationChannel(channelId)
        if (existingChannel == null) {
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Tabata Timer")
            .setContentText(content)
            .setSmallIcon(R.drawable.timer)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun updateNotification(content: String) {
        val notification = createNotification(content)
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private fun playBeepSound() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, R.raw.beep)
        mediaPlayer?.start()
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
    }

    private fun startForegroundService() {
        val notification = createNotification("Start the workout")
        startForeground(1, notification)
    }

    fun setPhases(phases: List<Phase>, workCircles: Int, totalRepeats: Int) {
        if(phases.isEmpty()) return
        this.workCircles = workCircles
        this.totalRepeats = totalRepeats
        _state.value = _state.value.copy(phases = phases,
            remainingTime = phases.first().duration,
            currentPhaseIndex = 0,
            currentCircle = 1,
            currentPhase = 1)
    }

    fun startTimer() {
        timerJob?.cancel()
        _state.value = _state.value.copy(isRunning = true)
        timerJob = CoroutineScope(Dispatchers.IO).launch {
            while (_state.value.isRunning && _state.value.remainingTime > 0) {
                delay(1000)
                val remainingTime = _state.value.remainingTime - 1
                _state.value = _state.value.copy(remainingTime = if(remainingTime >= 0) remainingTime else 0)

                if(_state.value.currentPhaseIndex + 1 == _state.value.phases.size) pauseTimer()
                else if (_state.value.remainingTime == 0) moveToNextPhase()
                else if(_state.value.remainingTime == 1) playBeepSound()

                updateNotification("Phase: " +
                        "${_state.value.phases[_state.value.currentPhaseIndex].name}, " +
                        "${_state.value.remainingTime} s remaining.")
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _state.value = _state.value.copy(isRunning = false)
        updateNotification("Timer is paused!")
    }

    fun resetTimer() {
        pauseTimer()
        _state.value = _state.value.copy(currentPhaseIndex = 0,
            remainingTime = _state.value.phases.firstOrNull()?.duration ?: 0,
            currentCircle = 1,
            currentPhase = 1)
    }

    fun moveToNextPhase() {
        val nextIndex = _state.value.currentPhaseIndex + 1
        if (nextIndex < _state.value.phases.size) {
            val currentPhase = _state.value.phases[nextIndex - 1]
            val nextPhase = _state.value.phases[nextIndex]
            var phaseNum = _state.value.currentPhase
            var circleNum = _state.value.currentCircle
            if(currentPhase.name == PHASE.Rest.name) {
                phaseNum += 1
                if(phaseNum > workCircles) {
                    phaseNum = 1
                    circleNum += 1
                }
            }
            _state.value = _state.value.copy(
                currentPhaseIndex = nextIndex,
                remainingTime = nextPhase.duration,
                currentPhase = phaseNum,
                currentCircle = circleNum
            )
        } else {
            resetTimer()
        }
    }

    fun moveToPrevPhase() {
        val prevIndex = _state.value.currentPhaseIndex - 1
        if (prevIndex >= 0) {
            val prevPhase = _state.value.phases[prevIndex]
            var phaseNum = _state.value.currentPhase
            var circleNum = _state.value.currentCircle
            if(prevPhase.name == PHASE.Rest.name) {
                phaseNum -= 1
                if(phaseNum == 0) {
                    phaseNum = this.workCircles
                    circleNum -= 1
                }
            }
            _state.value = _state.value.copy(
                currentPhaseIndex = prevIndex,
                remainingTime = _state.value.phases[prevIndex].duration,
                currentPhase = phaseNum,
                currentCircle = circleNum
            )
        }
    }
}