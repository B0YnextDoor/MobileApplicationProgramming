package com.example.converter.android.components

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Straighten
import com.example.converter.android.ui.currency.CurrencyConverterScreen
import com.example.converter.android.ui.distance.DistanceConverterScreen
import com.example.converter.android.ui.weight.WeightConverterScreen

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    data object Currency : Screen("currency", "Currency", Icons.Filled.AttachMoney)
    data object Weight : Screen("weight", "Weight", Icons.Filled.Scale)
    data object Distance : Screen("distance", "Distance", Icons.Filled.Straighten)
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Currency.route) {
        composable(Screen.Currency.route) { CurrencyConverterScreen() }
        composable(Screen.Weight.route) { WeightConverterScreen() }
        composable(Screen.Distance.route) { DistanceConverterScreen() }
    }
}
