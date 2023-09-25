package com.awesomejim.weatherforecast.ui.screens.search



data class SearchUiState(
    val searchKeyWord: String = "",
    val isSearchComplete: Boolean = false,
    val isSearchWordValid: Boolean = false
)
