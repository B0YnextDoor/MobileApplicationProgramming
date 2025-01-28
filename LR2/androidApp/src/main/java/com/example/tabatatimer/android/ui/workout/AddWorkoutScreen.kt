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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tabatatimer.android.components.TopBar
import com.example.tabatatimer.android.domain.Workout
import com.example.tabatatimer.android.models.WorkoutViewModel
import com.example.tabatatimer.android.utils.LocalSnackbarHostState
import com.example.tabatatimer.android.utils.translate
import kotlinx.coroutines.launch

@Composable
fun AddWorkoutScreen(navController: NavController, viewModel: WorkoutViewModel) {
    var workoutName by rememberSaveable { mutableStateOf("") }
    var selectedColor by rememberSaveable { mutableIntStateOf(Color.Red.toArgb()) }

    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan, Color.Magenta)

    val snackbarHostState = LocalSnackbarHostState.current
    val coroutineScope = rememberCoroutineScope()
    val fontSize by viewModel.fontSize.collectAsState()
    val locale by viewModel.locale.collectAsState()

    val onAddWorkout = {
        viewModel.addWorkout(
            Workout(title = workoutName, color = selectedColor)
        )
        navController.popBackStack()
        coroutineScope.launch {
            snackbarHostState.showSnackbar(translate(locale, "Workout added"))
        }
    }

    Scaffold(
        topBar = { TopBar(translate(locale, "Add New Workout")) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = workoutName,
                onValueChange = { workoutName = it },
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

            LazyRow(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(colors) { color ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(color)
                            .clickable { selectedColor = color.toArgb() }
                            .border(
                                width = if (selectedColor == color.toArgb()) 3.dp else 0.dp,
                                color = Color.DarkGray,
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onAddWorkout() },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(4.dp),
                enabled = workoutName.isNotBlank()
            ) {
                Text(
                    translate(locale, "Create Workout"), style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = fontSize.sp
                ))
            }
        }
    }
}