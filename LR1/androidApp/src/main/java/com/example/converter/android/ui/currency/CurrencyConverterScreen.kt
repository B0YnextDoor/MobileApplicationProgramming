package com.example.converter.android.ui.currency

import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.converter.android.models.CurrencyConverterViewModel
import com.example.converter.android.utils.NetworkUtils
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CurrencyConverterScreen(viewModel: CurrencyConverterViewModel = viewModel()) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        val isInternetAvailable = NetworkUtils.isInternetAvailable(context)
        viewModel.loadCurrencyRates(isInternetAvailable)
    }

    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    if(isPortrait) {
        PortraitLayout(
            uiState.converterState.from, uiState.converterState.to, uiState.converterState.amount,
            uiState.converterState.convertedAmount, uiState.currencyFlags, viewModel::updateAmount,
            viewModel::onFromCurrencyChanged, viewModel::onToCurrencyChanged,
            viewModel::switchCurrencies, uiState.errorMessage
        )
    }
    else {
        LandscapeLayout(
            uiState.converterState.from, uiState.converterState.to, uiState.converterState.amount,
            uiState.converterState.convertedAmount, uiState.currencyFlags, viewModel::updateAmount,
            viewModel::onFromCurrencyChanged, viewModel::onToCurrencyChanged,
            viewModel::switchCurrencies, uiState.errorMessage
        )
    }
}

@Preview
@Composable
fun PreviewCurrencyConverter() {
    CurrencyConverterScreen()
}
