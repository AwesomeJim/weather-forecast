package com.awesomejim.weatherforecast.feature.bookmarks

import com.awesomejim.weatherforecast.core.LocationItemData

data class SearchUiState(
    val searchKeyWord: String = "",
    val isSearching: Boolean = false,
    val isSearchingSuccessful: Boolean = false,
    val isSearchWordValid: Boolean = true,
    val searchResultWeatherData: LocationItemData? = null,
    val isSearchError: Boolean = false,
    val searchErrorMessage: Int? = null
)
