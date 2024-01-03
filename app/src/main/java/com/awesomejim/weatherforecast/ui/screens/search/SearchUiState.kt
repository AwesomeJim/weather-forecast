package com.awesomejim.weatherforecast.ui.screens.search

data class SearchUiState(
    val searchKeyWord: String = "",
    val isSearching: Boolean = false,
    val isSearchingSuccessful: Boolean = false,
    val isSearchWordValid: Boolean = true,
    val searchResultWeatherData: com.awesomejim.weatherforecast.core.LocationItemData? = null,
    val isSearchError: Boolean = false,
    val searchErrorMessage: Int? = null
)
