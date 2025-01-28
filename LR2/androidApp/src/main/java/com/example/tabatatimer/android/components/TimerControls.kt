package com.example.tabatatimer.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness1
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.tabatatimer.android.models.TimerViewModel

@Composable
fun PhaseRow(viewModel: TimerViewModel) {
    val state by viewModel.uiState.collectAsState()

    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp).fillMaxWidth()) {
        IconButton(onClick = { viewModel.resetTimer() }, modifier = Modifier.size(60.dp)) {
            Icon(
                Icons.Filled.Stop, contentDescription = "Reset timer",
                tint = Color.White,
                modifier = Modifier.fillMaxSize().clip(CircleShape).padding(6.dp))
        }
        Text(text = state.phases[state.currentPhaseIndex].name,
            style = TextStyle(color = Color.White, fontSize = 40.sp,
                fontWeight = FontWeight(600)))

        IconButton(onClick = { if(state.isRunning) viewModel.pauseTimer() else viewModel.startTimer() },
            modifier = Modifier.size(60.dp)) {
            Icon(
                if(state.isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                contentDescription = "Play/Pause",
                tint = Color.White,
                modifier = Modifier.fillMaxSize().clip(CircleShape).padding(6.dp))
        }
    }
}

@Composable
fun TimerControls(viewModel: TimerViewModel, bgColor: Color) {
    val state by viewModel.uiState.collectAsState()

    Row(horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .background(bgColor)) {
        IconButton(onClick = { viewModel.moveToPrevPhase() }, modifier = Modifier.size(50.dp)) {
            Icon(
                Icons.Filled.KeyboardDoubleArrowLeft, contentDescription = "Prev phase",
                tint = Color.White,
                modifier = Modifier.fillMaxSize().clip(CircleShape).padding(6.dp))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            if(viewModel.totalRepeats > 1) {
                Text(text="${state.currentCircle}/${viewModel.totalRepeats}",
                    style = TextStyle(color = Color.White, fontSize = 30.sp,
                        fontWeight = FontWeight(600)))
                Icon(Icons.Filled.Brightness1, contentDescription = null, tint = Color.White,
                    modifier = Modifier.size(10.dp).clip(CircleShape))
            }
            Text(text="${state.currentPhase}/${viewModel.workCircles}",
                style = TextStyle(color = Color.White, fontSize = 30.sp,
                fontWeight = FontWeight(600)))
        }
        IconButton(onClick = { viewModel.moveToNextPhase() }, modifier = Modifier.size(50.dp)) {
            Icon(
                Icons.Filled.KeyboardDoubleArrowRight, contentDescription = "Next phase",
                tint = Color.White,
                modifier = Modifier.fillMaxSize().clip(CircleShape).padding(6.dp))
        }
    }
}