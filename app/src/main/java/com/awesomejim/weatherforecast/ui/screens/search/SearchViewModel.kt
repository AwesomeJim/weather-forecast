package com.awesomejim.weatherforecast.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.weatherforecast.data.SettingsRepository
import com.awesomejim.weatherforecast.data.WeatherRepository
import com.awesomejim.weatherforecast.di.network.RetrialResult
import com.awesomejim.weatherforecast.ui.CurrentWeatherUiState
import com.awesomejim.weatherforecast.ui.common.toResourceId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
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

    var searchKeyWord by mutableStateOf("")
        private set


    private val _searchWeatherUiState =
        MutableStateFlow<CurrentWeatherUiState>(CurrentWeatherUiState.Loading)

    val searchWeatherUiState: StateFlow<CurrentWeatherUiState> =
        _searchWeatherUiState.asStateFlow()

    init {
        _uiState.value = SearchUiState(searchKeyWord = "")
        viewModelScope.launch {
            preferredUnits = settingsRepository.getUnits().first()
        }
    }


    fun updateUserSearchKeyWord(keyWord: String) {
        searchKeyWord = keyWord
        _uiState.update { currentState ->
            currentState.copy(searchKeyWord = keyWord)
        }
    }


    /**
     * Fetch 5 days forecast weather data for the current location
     *
     */
     fun fetchForecastCurrentWeatherData() {
        Timber.e("fetchWeatherDataWithCoordinates result:: $searchKeyWord")
        viewModelScope.launch {
            if (searchKeyWord.isNotEmpty() && !isLoadingData) {
                val result = defaultWeatherRepository.fetchWeatherDataWithLocationQuery(
                    searchKeyWord,
                    preferredUnits
                )
                isLoadingData = true
                Timber.e("fetchWeatherDataWithCoordinates result:: $result")
                when (result) {
                    is RetrialResult.Success -> {
                        val weatherData = result.data
                        Timber.e("weatherData result:: ${result.data}")
                        _searchWeatherUiState.emit(CurrentWeatherUiState.Success(weatherData))
                    }

                    is RetrialResult.Error -> {
                        CurrentWeatherUiState.Error(result.errorType.toResourceId())
                        Timber.e("Error :: ${result.errorType.toResourceId()}")
                    }
                }
                _uiState.update { currentState ->
                    currentState.copy(isSearchComplete = true)
                }
            }
        }
    }

}