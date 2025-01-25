package com.example.converter.android.ui.distance

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.converter.android.models.DistanceConverterViewModel
import com.example.converter.android.ui.layout.LandscapeLayout
import com.example.converter.android.ui.layout.PortraitLayout
import com.example.converter.converters.convert
import com.example.converter.converters.distanceRates
import java.util.Locale

@Composable
fun DistanceConverterScreen(viewModel: DistanceConverterViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    if(isPortrait) {
        PortraitLayout(uiState.from, uiState.to, uiState.amount, uiState.convertedAmount,
            uiState.values, viewModel::updateAmount, viewModel::onFromDistanceChanged,
            viewModel::onToDistanceChanged, viewModel::switchDistances)
    }
    else {
        LandscapeLayout(uiState.from, uiState.to, uiState.amount, uiState.convertedAmount,
            uiState.values, viewModel::updateAmount, viewModel::onFromDistanceChanged,
            viewModel::onToDistanceChanged, viewModel::switchDistances)
    }
}

@Preview
@Composable
fun PreviewDistanceConverter() {
    DistanceConverterScreen()
}
