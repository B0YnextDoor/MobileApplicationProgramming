package com.example.tabatatimer.android.components

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.tabatatimer.android.models.WorkoutViewModel
import com.example.tabatatimer.android.ui.home.HomeScreen
import com.example.tabatatimer.android.ui.splash.SplashScreen
import com.example.tabatatimer.android.ui.timer.TimerScreen
import com.example.tabatatimer.android.ui.workout.AddWorkoutScreen
import com.example.tabatatimer.android.ui.workout.EditWorkoutScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Timer : Screen("timer")
    data object Add : Screen("add")
    data object Edit : Screen("edit")
}

@Composable
fun AppNavGraph(navController: NavHostController, viewModel : WorkoutViewModel) {
    NavHost(navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen({
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Home.route) { HomeScreen(navController, viewModel) }
        composable(Screen.Timer.route) { TimerScreen(navController, viewModel) }
        composable(Screen.Add.route) { AddWorkoutScreen(navController, viewModel) }
        composable(Screen.Edit.route) { EditWorkoutScreen(navController, viewModel) }
    }
}
