package com.example.tabatatimer.android.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabatatimer.android.TabataApplication
import com.example.tabatatimer.android.domain.Workout
import com.example.tabatatimer.android.domain.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val workoutRepository: WorkoutRepository

    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    private val _workout = MutableStateFlow(Workout(title = "", color = 0))
    val workouts: StateFlow<List<Workout>> get() = _workouts
    val workout: StateFlow<Workout> get() = _workout

    init {
        val workoutDao = (application as TabataApplication).database.workoutDao()
        workoutRepository = WorkoutRepository(workoutDao)
        getWorkouts()
    }

    private fun getWorkouts() {
        viewModelScope.launch {
            _workouts.value = workoutRepository.getWorkouts().reversed()
        }
    }

    fun addWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutRepository.insertWorkout(workout)
            getWorkouts()
        }
    }

    fun updateWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutRepository.updateWorkout(workout)
            getWorkouts()
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutRepository.deleteWorkout(workout)
            getWorkouts()
        }
    }

    fun setWorkout(workout: Workout) {
        _workout.value = workout
    }

    fun clearAll() {
        viewModelScope.launch {
            workoutRepository.clearAll()
            getWorkouts()
        }
    }

    private val _fontSize = MutableStateFlow(20f)
    val fontSize: StateFlow<Float> = _fontSize
    private val _locale = MutableStateFlow("en")
    val locale: StateFlow<String> = _locale

    fun setSize(size: Float) {
        _fontSize.value = size
    }

    fun setLocale(lang: String) {
        _locale.value = lang
    }
}
