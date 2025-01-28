package com.example.tabatatimer.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tabatatimer.android.domain.Workout
import com.example.tabatatimer.android.models.WorkoutViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.tabatatimer.android.utils.LocalSnackbarHostState
import com.example.tabatatimer.android.utils.translate

@Composable
fun WorkoutListItem(workout: Workout,
                    viewModel: WorkoutViewModel,
                    navController: NavController
) {
    val bgColor = Color(workout.color)
    val iconColor = if(listOf(Color.Yellow, Color.Cyan).contains(bgColor)) Color.Blue
                        else Color.White
    val snackbarHostState = LocalSnackbarHostState.current
    val coroutineScope = rememberCoroutineScope()
    val fontSize by viewModel.fontSize.collectAsState()
    val locale by viewModel.locale.collectAsState()

    val onPlay = {
        viewModel.setWorkout(workout)
        navController.navigate(Screen.Timer.route)
    }

    val onUpdate = {
        viewModel.setWorkout(workout)
        navController.navigate(Screen.Edit.route)
    }

    val onDelete = {
        viewModel.deleteWorkout(workout)
        coroutineScope.launch {
            snackbarHostState.showSnackbar(translate(locale, "Workout deleted"))
        }
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .height(150.dp)
        .padding(vertical = 4.dp)
        .border(2.dp, bgColor, RoundedCornerShape(8.dp))
        .background(bgColor, RoundedCornerShape(8.dp)),
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)) {
            Text(text = workout.title, style = TextStyle(fontWeight = FontWeight.Bold,
                fontSize = (1.5 * fontSize).sp, color = Color.White, shadow = Shadow(
                    color = Color.Black,
                    offset = androidx.compose.ui.geometry.Offset(3f, 3f),
                    blurRadius = 7f
                )
            ), modifier = Modifier.fillMaxWidth(0.65f), maxLines = 5,
                overflow = TextOverflow.Ellipsis)
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                IconButton(onClick = { onPlay() }) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Start", tint = iconColor,
                        modifier = Modifier.size(35.dp))
                }
                IconButton(onClick = { onUpdate() }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = iconColor,
                        modifier = Modifier.size(30.dp))
                }
                IconButton(onClick = { onDelete() }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = iconColor,
                        modifier = Modifier.size(30.dp))
                }
            }
        }
    }
}