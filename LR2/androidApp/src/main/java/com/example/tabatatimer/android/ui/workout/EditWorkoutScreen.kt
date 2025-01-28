package com.example.tabatatimer.android.ui.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tabatatimer.android.components.BackButton
import com.example.tabatatimer.android.components.TopBar
import com.example.tabatatimer.android.models.EditWorkoutViewModel
import com.example.tabatatimer.android.models.WorkoutViewModel
import com.example.tabatatimer.android.utils.LocalSnackbarHostState
import com.example.tabatatimer.android.utils.translate
import kotlinx.coroutines.launch

@Composable
fun EditWorkoutScreen(navController: NavController, workoutVM: WorkoutViewModel,
                      viewModel: EditWorkoutViewModel = viewModel()) {
    val workout by viewModel.uiState.collectAsState()
    val snackbarHostState = LocalSnackbarHostState.current
    val coroutineScope = rememberCoroutineScope()
    val fontSize by workoutVM.fontSize.collectAsState()
    val locale by workoutVM.locale.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.updateWorkout(workoutVM.workout.value)
    }

    Scaffold(topBar = { TopBar(translate(locale, "Edit Workout")) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Top,
        ) {
            BackButton { navController.popBackStack() }
            if(workout.id > 0) {
                LazyColumn(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    item {
                        TextField(
                            value = workout.title,
                            onValueChange = { viewModel.updateWorkout(workout.copy(title = it)) },
                            label = { Text(translate(locale, "Workout Name"), style = TextStyle(fontSize = 18.sp)) },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = fontSize.sp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .height(70.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        LazyRow(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(viewModel.colors.size) { idx ->
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(viewModel.colors[idx])
                                        .clickable {
                                            viewModel.updateWorkout(
                                                workout.copy(
                                                    color = viewModel.colors[idx].toArgb()
                                                )
                                            )
                                        }
                                        .border(
                                            width = if (workout.color ==
                                                viewModel.colors[idx].toArgb()
                                            ) 3.dp else 0.dp,
                                            color = Color.DarkGray,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        WorkoutNumberInput(
                            translate(locale, "Prepare Time (sec)"), workout.prepDuration.toString(),
                            { viewModel.updateWorkout(workout.copy(prepDuration = it)) })
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        WorkoutNumberInput(
                            translate(locale, "Work Time (sec)"), workout.workDuration.toString(),
                            { viewModel.updateWorkout(workout.copy(workDuration = it))})
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        WorkoutNumberInput(
                            translate(locale, "Rest Time (sec)"), workout.restDuration.toString(),
                            { viewModel.updateWorkout(workout.copy(restDuration = it))})
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        WorkoutNumberInput(
                            translate(locale, "Cooldown Time (sec)"), workout.cooldownDuration.toString(),
                            { viewModel.updateWorkout(workout.copy(cooldownDuration = it))}, true)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        WorkoutNumberInput(
                            translate(locale, "Work/Rest Cycles"), workout.workCircles.toString(),
                            { viewModel.updateWorkout(workout.copy(workCircles = it)) })
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        WorkoutNumberInput(
                            translate(locale, "Workout Repeats"), workout.totalRepeats.toString(),
                            { viewModel.updateWorkout(workout.copy(totalRepeats = it)) })
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        WorkoutNumberInput(
                            translate(locale, "Rest Between Repeats (sec)"), workout.repeatRest.toString(),
                            { viewModel.updateWorkout(workout.copy(repeatRest = it))}, true)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Button(
                            onClick = { viewModel.onSaveChanges(snackbarHostState,
                                coroutineScope, workoutVM) },
                            modifier = Modifier.fillMaxWidth(0.7f),
                            enabled = viewModel.isButtonEnabled()
                        ) {
                            Text(
                                translate(locale, "Save Changes"), style = TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = fontSize.sp
                            ))
                        }
                    }
                }
            } else {
                Text(text = translate(locale, "Workout not found :("),
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight(600),
                        fontSize = (1.75 * fontSize).sp
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp))
            }
        }
    }
}


@Composable
fun WorkoutNumberInput(label: String, value: String, onValueChange: (Int) -> Unit,
                       zeroAllowed: Boolean = false) {
    val snackbarHostState = LocalSnackbarHostState.current
    val coroutineScope = rememberCoroutineScope()

    fun validateInput(value: String) {
        if (value.all { char -> char.isDigit() }) {
            val newValue = value.toIntOrNull()
            if(newValue == null && zeroAllowed) {
                onValueChange(0)
            }
            else if(newValue != null && newValue <= 3600 &&
                (newValue > 0 || (zeroAllowed && newValue == 0)) ) {
                onValueChange(newValue)
            } else {
                val message = if(newValue == null) "$label is required!" else if (newValue > 3600)
                    "Max value is 3600 sec!" else "$label must be a positive integer!"
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

    TextField(
        value = value,
        onValueChange = { validateInput(it) },
        label = { Text(label, style = TextStyle(fontSize = 18.sp)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(
            fontSize = 25.sp
        ),
        modifier = Modifier.fillMaxWidth().padding(4.dp).height(70.dp)
    )
}