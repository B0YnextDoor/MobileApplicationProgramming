package com.example.tabatatimer.android.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AddWorkoutButton(navController: NavController) {
    FloatingActionButton(onClick = {
        navController.navigate(Screen.Add.route)
    }, contentColor = Color.White, containerColor = Color.Red,
        modifier = Modifier.size(70.dp),
        shape = CircleShape)
    {
        Icon(Icons.Filled.Add, contentDescription = "Add Workout")
    }
}