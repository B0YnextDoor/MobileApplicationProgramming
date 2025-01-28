package com.example.tabatatimer.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.tabatatimer.android.components.AppNavGraph
import com.example.tabatatimer.android.models.WorkoutViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.preference.PreferenceManager
import com.example.tabatatimer.android.utils.LocalSnackbarHostState
import com.example.tabatatimer.android.utils.defineFunctions
import com.example.tabatatimer.android.utils.setLocale
import java.util.Locale


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: WorkoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        applyLocale()
        super.onCreate(savedInstanceState)
        setContent {
            viewModel = viewModel()
            defineFunctions(viewModel)
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                MyApplicationTheme(applySavedTheme()) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Scaffold(snackbarHost = {SnackbarHost(hostState = snackbarHostState)})
                        {  _ ->
                            AppNavGraph(navController, viewModel)
                        }
                    }
                }
            }

        }
    }

    private fun applySavedTheme(): Boolean {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val themeValue = sharedPrefs.getString("theme", "light")
        val nightMode = when (themeValue) {
            "dark" -> AppCompatDelegate.MODE_NIGHT_YES
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            "system" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
        return nightMode == AppCompatDelegate.MODE_NIGHT_YES
    }

    private fun applyLocale() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val language = sharedPrefs.getString("language", "en") ?: "en"
        setLocale(language)
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}


@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MainActivity()
    }
}
