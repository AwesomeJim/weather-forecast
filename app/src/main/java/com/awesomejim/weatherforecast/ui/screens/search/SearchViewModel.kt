package com.awesomejim.weatherforecast.ui.screens.search

import android.text.TextUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.weatherforecast.data.SettingsRepository
import com.awesomejim.weatherforecast.data.WeatherRepository
import com.awesomejim.weatherforecast.di.network.RetrialResult
import com.awesomejim.weatherforecast.ui.common.toResourceId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val defaultWeatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private lateinit var preferredUnits: String
    private var isLoadingData = false

    // Search UI state
    private val _uiState = MutableStateFlow(SearchUiState())

    // Backing property to avoid state updates from other classes
    val searchUiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchKeyWord by mutableStateOf("")


//    private val _searchWeatherUiState =
//        MutableStateFlow<CurrentWeatherUiState>(CurrentWeatherUiState.Loading)
//
//    val searchWeatherUiState: StateFlow<CurrentWeatherUiState> =
//        _searchWeatherUiState.asStateFlow()

    init {
        _uiState.value = SearchUiState(searchKeyWord = "")
        viewModelScope.launch {
            preferredUnits = settingsRepository.getUnits().first()
        }
    }


    fun updateUserSearchKeyWord(keyWord: String) {
        searchKeyWord = keyWord.trim()
        val isValidCityName = isValidName(keyWord)
        _uiState.update { currentState ->
            currentState.copy(
                searchKeyWord = keyWord,
                isSearchWordValid = isValidCityName
            )
        }
    }

    /*
     Reset to default
     */
    fun updateSearchStatus() {
        _uiState.update { currentState ->
            currentState.copy(
                searchKeyWord = "",
                isSearching = false,
                isSearchingSuccessful = false,
                isSearchWordValid = true,
                searchResultWeatherData = null,
                isSearchError = false,
                searchErrorMessage = null
            )
        }
    }


    /**
     * Fetch 5 days forecast weather data for the current location
     *
     */
    fun fetchForecastCurrentWeatherData() {
        _uiState.update { currentState ->
            currentState.copy(
                isSearching = true
            )
        }
        Timber.e("fetchWeatherDataWithCoordinates result:: $searchKeyWord")
        viewModelScope.launch {
            isLoadingData = true
            val result = defaultWeatherRepository.fetchWeatherDataWithLocationQuery(
                searchKeyWord,
                preferredUnits
            )
            Timber.e("fetchWeatherDataWithCoordinates result:: $result")
            when (result) {
                is RetrialResult.Success -> {
                    val weatherData = result.data
                    Timber.e("weatherData result:: ${result.data}")
                    _uiState.update { currentState ->
                        currentState.copy(
                            isSearching = false,
                            isSearchingSuccessful = true,
                            searchResultWeatherData = weatherData,
                            isSearchError = false,
                            searchErrorMessage = null
                        )
                    }
                }

                is RetrialResult.Error -> {
                    Timber.e("Error :: ${result.errorType.toResourceId()}")
                    _uiState.update { currentState ->
                        currentState.copy(
                            isSearching = false,
                            isSearchingSuccessful = false,
                            searchResultWeatherData = null,
                            isSearchError = true,
                            searchErrorMessage = result.errorType.toResourceId()
                        )
                    }
                }

            }
        }
    }

    private fun isValidName(cityName: String): Boolean {
        return !((TextUtils.isEmpty(cityName) || cityName.length < 3)) && Pattern.matches(
            "^[A-Za-z0-9]+\\w$",
            cityName
        )
    }

}