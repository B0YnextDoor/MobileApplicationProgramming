package com.example.tabatatimer.android.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tabatatimer.android.components.AddWorkoutButton
import com.example.tabatatimer.android.components.TopBar
import com.example.tabatatimer.android.components.WorkoutListItem
import com.example.tabatatimer.android.models.WorkoutViewModel
import com.example.tabatatimer.android.utils.translate

@Composable
fun HomeScreen(navController: NavController, viewModel: WorkoutViewModel) {
    val workouts by viewModel.workouts.collectAsState()
    val fontSize by viewModel.fontSize.collectAsState()
    val locale by viewModel.locale.collectAsState()

    Scaffold(
        topBar = { TopBar(translate(locale, "Workout List"), navController = navController, fontSize = fontSize) },
        floatingActionButton = { AddWorkoutButton(navController) },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y=20.dp)
                .padding(padding),
            verticalArrangement = Arrangement.Top
        ) {
            if(workouts.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()){
                    items(workouts.size) { idx ->
                        WorkoutListItem(workouts[idx], viewModel, navController)
                    }
                }
            } else {
                Text(text = translate(locale, "You have no workouts yet..."),
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