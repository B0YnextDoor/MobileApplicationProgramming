package com.example.converter.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.*
import com.example.converter.android.components.Screen
import com.example.converter.android.components.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val screens = listOf(Screen.Currency, Screen.Weight, Screen.Distance)
    var selectedIndex by remember { mutableStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }


    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    screens.forEachIndexed { index, screen ->
                        NavigationBarItem(
                            label = { Text(screen.title) },
                            selected = selectedIndex == index,
                            onClick = {
                                selectedIndex = index
                                navController.navigate(screen.route)
                            },
                            icon = { Icon(screen.icon, contentDescription = screen.title) }
                        )
                    }
                }
            },
            snackbarHost = {SnackbarHost(hostState = snackbarHostState)}
        ) { paddingValues ->
            AppNavigation(navController)
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MainScreen()
    }
}
