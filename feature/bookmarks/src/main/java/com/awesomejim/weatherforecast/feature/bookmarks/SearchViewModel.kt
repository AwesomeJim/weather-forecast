package com.awesomejim.weatherforecast.feature.bookmarks

import android.text.TextUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.weatherforecast.core.DefaultLocation
import com.awesomejim.weatherforecast.core.LocationItemData
import com.awesomejim.weatherforecast.core.data.SettingsRepository
import com.awesomejim.weatherforecast.core.data.WeatherRepository
import com.awesomejim.weatherforecast.core.data.source.local.LocalDataSource
import com.awesomejim.weatherforecast.core.data.source.local.MediatorRepository
import com.awesomejim.weatherforecast.core.data.source.mapper.toResourceId
import com.awesomejim.weatherforecast.core.data.utils.RetrialResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val defaultWeatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository,
    private val mediatorRepository: MediatorRepository,
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private lateinit var preferredUnits: String
    private var isLoadingData = false

    // Search UI state
    private val _uiState = MutableStateFlow(SearchUiState())

    // Backing property to avoid state updates from other classes
    val searchUiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchKeyWord by mutableStateOf("")

    val savedLocationListUiState: StateFlow<SavedLocationListUiState> =
        localDataSource.loadAllLocation().map { list ->
            if (list != null) {
                SavedLocationListUiState(list)
            } else {
                SavedLocationListUiState()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = SavedLocationListUiState()
        )

    /**
     * set the initial search UI State
     */
    init {
        _uiState.value = SearchUiState(searchKeyWord = "")
        viewModelScope.launch {
            preferredUnits = settingsRepository.getUnits().first()
        }
    }

    /**
     * Save location to the Local Database
     *
     * @param locationItemData location to be saved
     */
    fun saveLocation(locationItemData: LocationItemData) {
        viewModelScope.launch {
            localDataSource.insertLocation(locationItemData)
        }
    }

    /**
     * Removes an location from the db.
     * @param locationItemData The location Item to be removed.
     */
    fun deleteLocationItem(locationItemData: LocationItemData) {
        viewModelScope.launch {
            localDataSource.deleteLocation(locationItemData)
        }
    }

    /**
     * Refresh saved location by fetching update data
     *
     * @param locationItemData
     */
    fun refreshWeatherData(locationItemData: LocationItemData) {
        viewModelScope.launch {
            val location = DefaultLocation(
                longitude = locationItemData.locationLongitude,
                latitude = locationItemData.locationLatitude
            )
            val result = mediatorRepository.fetchWeatherDataWithCoordinates(
                location,
                preferredUnits,
                locationId = locationItemData.locationId
            )
            Timber.e("fetchWeatherDataWithCoordinates result:: $result")
            when (result) {
                is RetrialResult.Success -> {
                    Timber.e("weatherData result:: ${result.data}")
                }

                is RetrialResult.Error -> {
                    Timber.e("Error :: ${result.errorType.toResourceId()}")
                }
            }
        }
    }

    /**
     * Update user typed search keyword
     *
     * @param keyWord
     */
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

    /**
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

    /**
     * Validate cityName as they user is typing
     *
     * @param cityName
     * @return
     */
    private fun isValidName(cityName: String): Boolean {
        return !((TextUtils.isEmpty(cityName) || cityName.length < 3)) && Pattern.matches(
            "^[A-Za-z0-9]+\\w$",
            cityName
        )
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Saved data Ui State for HomeScreen
 */
data class SavedLocationListUiState(val itemList: List<LocationItemData> = listOf())
