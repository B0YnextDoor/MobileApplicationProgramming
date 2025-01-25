package com.example.converter.android.ui.weight

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.converter.android.models.WeightConverterViewModel
import com.example.converter.android.ui.layout.LandscapeLayout
import com.example.converter.android.ui.layout.PortraitLayout

@Composable
fun WeightConverterScreen(viewModel: WeightConverterViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    if(isPortrait) {
        PortraitLayout(uiState.from, uiState.to, uiState.amount, uiState.convertedAmount,
            uiState.values, viewModel::updateAmount, viewModel::onFromWeightChanged,
            viewModel::onToWeightChanged, viewModel::switchWeights)
    }
    else {
        LandscapeLayout(uiState.from, uiState.to, uiState.amount, uiState.convertedAmount,
            uiState.values, viewModel::updateAmount, viewModel::onFromWeightChanged,
            viewModel::onToWeightChanged, viewModel::switchWeights)
    }
}

@Preview
@Composable
fun PreviewWeightConverter() {
   WeightConverterScreen()
}
