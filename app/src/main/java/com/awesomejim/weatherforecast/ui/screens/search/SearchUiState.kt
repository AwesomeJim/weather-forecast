package com.awesomejim.weatherforecast.ui.screens.search

import com.awesomejim.weatherforecast.data.model.LocationItemData

data class SearchUiState(
    val searchKeyWord: String = "",
    val isSearching: Boolean = false,
    val isSearchingSuccessful: Boolean = false,
    val isSearchWordValid: Boolean = true,
    val searchResultWeatherData: LocationItemData? = null,
    val isSearchError: Boolean = false,
    val searchErrorMessage: Int? = null
)
