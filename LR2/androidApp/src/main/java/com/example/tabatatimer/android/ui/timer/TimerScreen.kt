package com.example.tabatatimer.android.ui.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tabatatimer.android.components.BackButton
import com.example.tabatatimer.android.components.PhaseRow
import com.example.tabatatimer.android.components.TimerControls
import com.example.tabatatimer.android.components.TopBar
import com.example.tabatatimer.android.domain.PHASE
import com.example.tabatatimer.android.domain.Workout
import com.example.tabatatimer.android.models.TimerViewModel
import com.example.tabatatimer.android.models.WorkoutViewModel
import com.example.tabatatimer.android.services.Phase
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TimerScreen(navController: NavController, workoutVM: WorkoutViewModel,
                viewModel: TimerViewModel = viewModel()) {
    val timer by viewModel.uiState.collectAsState()
    val workout by workoutVM.workout.collectAsState()
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val colorScheme = MaterialTheme.colorScheme
    val bgColor = rememberSaveable { mutableStateOf(Color(0xFF063e29).toArgb()) }

    LaunchedEffect(Unit) {
        viewModel.bindService(context) {
            viewModel.setPhases(createPhaseList(workout), workout.workCircles, workout.totalRepeats)
        }
    }

    LaunchedEffect(timer.currentPhaseIndex) {
        val phaseIndex = timer.currentPhaseIndex
        if(timer.phases.isNotEmpty()) {
            val phaseName = timer.phases[phaseIndex].name
            bgColor.value = when (phaseName) {
                PHASE.Preparing.name -> Color(0xFF063e29).toArgb()
                PHASE.Working.name -> Color.Red.toArgb()
                PHASE.Rest.name -> Color.Blue.toArgb()
                else -> colorScheme.primary.toArgb()
            }
        }
        snapshotFlow { phaseIndex }
            .collectLatest { index ->
                listState.animateScrollToItem(index)
            }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.unbindService(context) }
    }

    Scaffold(
        topBar = { TopBar("Workout", bgColor = Color(bgColor.value)) },
        bottomBar = { TimerControls(viewModel, Color(bgColor.value)) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(bgColor.value)),
        ) {
            BackButton {
                viewModel.resetTimer()
                navController.popBackStack()
            }
            Column(modifier = Modifier.fillMaxSize().padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                if(timer.phases.isNotEmpty()) {
                    PhaseRow(viewModel)
                    Text(text = timer.remainingTime.toString(),
                        style = TextStyle(color = Color.White, fontSize = 200.sp,
                            fontWeight = FontWeight.Bold,),
                        modifier = Modifier.padding(vertical = 16.dp))
                    LazyColumn(state = listState,
                        modifier = Modifier.fillMaxSize().padding(vertical = 8.dp)) {
                        items(timer.phases.count()) { idx ->
                            val phase = timer.phases[idx]
                            Text(text="${idx + 1}. ${phase.name}" +
                                    if(phase.duration > 0) ": ${phase.duration}" else "",
                                style = TextStyle(color = Color.White, fontSize = 40.sp,
                                    fontWeight = FontWeight(600)),
                                modifier = Modifier.height(70.dp)
                                    .background(
                                        color = if (idx == timer.currentPhaseIndex)
                                            Color(0, 0, 0, 99)
                                        else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .clip(RoundedCornerShape(8.dp)))
                        }
                    }
                } else {
                    Text(text = "Fetching workout error :(", fontWeight = FontWeight(600),
                        fontSize = 25.sp, modifier = Modifier.padding(horizontal = 4.dp))
                }
            }
        }
    }
}

fun createPhaseList(workout: Workout): List<Phase> {
    val phases: MutableList<Phase> = mutableListOf(Phase(PHASE.Preparing.name, workout.prepDuration))
    for(rep in 0..<workout.totalRepeats) {
        for(i in 0..<workout.workCircles) {
            phases += Phase(PHASE.Working.name, workout.workDuration)
            if(i != workout.workCircles - 1)
                phases += Phase(PHASE.Rest.name, workout.restDuration)
        }
        if(rep != workout.totalRepeats - 1)
            phases += if(workout.repeatRest == 0) Phase(PHASE.Rest.name, workout.restDuration) else
                Phase("Rest between repeats...", workout.repeatRest)
    }
    phases += listOf(if(workout.cooldownDuration == 0) Phase(PHASE.Rest.name, workout.restDuration) else
        Phase(PHASE.Cooldown.name, workout.cooldownDuration), Phase(PHASE.Finish.name, 0))
    return phases.toList()
}