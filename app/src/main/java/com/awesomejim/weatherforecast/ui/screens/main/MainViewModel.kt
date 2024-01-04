package com.awesomejim.weatherforecast.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.weatherforecast.core.LocationItemData
import com.awesomejim.weatherforecast.core.data.source.mapper.toResourceId
import com.awesomejim.weatherforecast.core.network.RetrialResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val defaultWeatherRepository: com.awesomejim.weatherforecast.core.data.WeatherRepository,
    private val settingsRepository: com.awesomejim.weatherforecast.core.data.SettingsRepository
) : ViewModel() {

    private lateinit var currentLocation: com.awesomejim.weatherforecast.core.DefaultLocation
    private lateinit var preferredUnits: String
    private var isLoadingData = false

    private val _state = MutableStateFlow(MainViewState())
    val state: StateFlow<MainViewState> = _state.asStateFlow()

    private val _currentWeatherUiState =
        MutableStateFlow<CurrentWeatherUiState>(CurrentWeatherUiState.Loading)

    val currentWeatherUiState: StateFlow<CurrentWeatherUiState> =
        _currentWeatherUiState.asStateFlow()

    private val _forecastListState =
        MutableStateFlow<List<LocationItemData>>(emptyList())

    val forecastListState: StateFlow<List<LocationItemData>> =
        _forecastListState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                settingsRepository.getUnits(),
                settingsRepository.getDefaultLocation()
            ) { pUnits, defaultLocation ->
                Pair(pUnits, defaultLocation)
            }.collect { (pUnits, defaultLocation) ->
                preferredUnits = pUnits
                currentLocation = defaultLocation
            }
        }
    }

    /** The mutable State that stores the status of the most recent request */
    private fun fetchCurrentWeatherData() {
        viewModelScope.launch {
            val result = defaultWeatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = currentLocation, units = preferredUnits
            )
            _currentWeatherUiState.emit(processCurrentWeatherResult(result))
        }
    }

    /**
     * Fetch 5 days forecast weather data for the current location
     *
     */
    private fun fetchForecastCurrentWeatherData() {
        viewModelScope.launch {
            Timber.tag("MainViewModel")
                .e("fetchWeatherDataWithCoordinates :: %s", currentLocation.latitude)
            val result = defaultWeatherRepository.fetchWeatherForecastWithCoordinates(
                defaultLocation = currentLocation, units = preferredUnits,
            )
            Timber.e("fetchWeatherDataWithCoordinates result:: $result")
            when (result) {
                is com.awesomejim.weatherforecast.core.network.RetrialResult.Success -> {
                    val weatherData = result.data
                    Timber.e("weatherData result:: ${result.data}")
                    Timber.i("Size :: ${weatherData.size}")
                    _forecastListState.emit(weatherData)
                }

                is com.awesomejim.weatherforecast.core.network.RetrialResult.Error -> {
                    Timber.e("Error :: ${result.errorType.toResourceId()}")
                }
            }
        }
    }

    fun processIntent(mainViewUiState: MainViewUiState) {
        Timber.tag("MainViewModel").e("processIntent called $mainViewUiState")
        when (mainViewUiState) {
            is MainViewUiState.GrantPermission -> {
                setState { copy(isPermissionGranted = mainViewUiState.isGranted) }
            }

            is MainViewUiState.CheckLocationSettings -> {
                setState { copy(isLocationSettingEnabled = mainViewUiState.isEnabled) }
            }

            is MainViewUiState.ReceiveLocation -> {
                val defaultLocation = com.awesomejim.weatherforecast.core.DefaultLocation(
                    longitude = mainViewUiState.longitude,
                    latitude = mainViewUiState.latitude
                )
                this.currentLocation = defaultLocation
                Timber.tag("MainViewModel").e("defaultLocation ${defaultLocation.latitude}")
                viewModelScope.launch {
                    settingsRepository.setDefaultLocation(defaultLocation)
                }
                setState { copy(defaultLocation = defaultLocation) }
            }
        }
    }

    private fun setState(stateReducer: MainViewState.() -> MainViewState) {
        viewModelScope.launch {
            _state.emit(stateReducer(state.value))
        }
    }

    private fun processCurrentWeatherResult(result: com.awesomejim.weatherforecast.core.network.RetrialResult<LocationItemData>):
        CurrentWeatherUiState {
        isLoadingData = false
        return when (result) {
            is com.awesomejim.weatherforecast.core.network.RetrialResult.Success -> {
                val weatherData = result.data
                Timber.e("weatherData result:: ${result.data}")
                Timber.i("locationName :: ${weatherData.locationName}")
                CurrentWeatherUiState.Success(result.data)
            }

            is com.awesomejim.weatherforecast.core.network.RetrialResult.Error -> {
                Timber.e("Error :: ${result.errorType.toResourceId()}")
                CurrentWeatherUiState.Error(result.errorType.toResourceId())
            }
        }
    }

    fun processUiState(currentWeatherUiState: CurrentWeatherUiState) {
        when (currentWeatherUiState) {
            is CurrentWeatherUiState.Error -> {
                isLoadingData = false
            }

            CurrentWeatherUiState.Loading -> {
                Timber.tag("MainViewModel").e("fetchWeatherData::- $currentWeatherUiState")
                fetchWeatherData()
                isLoadingData = true
            }

            is CurrentWeatherUiState.Success -> {
                isLoadingData = false
            }
        }
    }

    fun fetchWeatherData() {
        Timber.tag("MainViewModel").e("fetchWeatherData::-")
        if (this::currentLocation.isInitialized) {
            if (!isLoadingData) {
                _currentWeatherUiState.value = CurrentWeatherUiState.Loading
                fetchCurrentWeatherData()
                fetchForecastCurrentWeatherData()
            }
        } else {
            Timber.tag("MainViewModel").e("No Location Details")
        }
    }
}

data class MainViewState(
    val isPermissionGranted: Boolean = false,
    val isLocationSettingEnabled: Boolean = false,
    val defaultLocation: com.awesomejim.weatherforecast.core.DefaultLocation? = null
)

/**
 * Ui State for HomeScreen
 */
sealed class MainViewUiState {

    data class GrantPermission(val isGranted: Boolean) : MainViewUiState()

    data class CheckLocationSettings(val isEnabled: Boolean) : MainViewUiState()

    data class ReceiveLocation(val latitude: Double, val longitude: Double) : MainViewUiState()
}

sealed interface CurrentWeatherUiState {
    data class Success(val currentWeather: LocationItemData) : CurrentWeatherUiState
    data class Error(val errorMessageId: Int) : CurrentWeatherUiState
    object Loading : CurrentWeatherUiState
}
