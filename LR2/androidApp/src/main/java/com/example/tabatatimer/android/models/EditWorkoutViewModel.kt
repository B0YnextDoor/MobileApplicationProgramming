package com.example.tabatatimer.android.models


import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.example.tabatatimer.android.domain.Workout
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class EditWorkoutViewModel : ViewModel() {
    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan, Color.Magenta)

    private val _uiState = MutableStateFlow(Workout(title = "", color = 0))
    val uiState: StateFlow<Workout> = _uiState

    fun updateWorkout(workout: Workout?) {
        if(workout != null && workout.id > 0)
            _uiState.value = workout.copy()
    }

    fun isButtonEnabled(): Boolean {
        val workout = _uiState.value
        return workout.title.isNotEmpty() && workout.prepDuration > 0 &&
                workout.workDuration > 0 && workout.restDuration > 0 && workout.cooldownDuration >= 0
                && workout.workCircles > 0 && workout.repeatRest >= 0 && workout.totalRepeats > 0
    }

    fun onSaveChanges(snackbarHostState: SnackbarHostState,
                      coroutineScope: CoroutineScope,
                      viewModel: WorkoutViewModel) {
        viewModel.updateWorkout(_uiState.value)
        coroutineScope.launch {
            snackbarHostState.showSnackbar("Workout updated")
        }
    }

}