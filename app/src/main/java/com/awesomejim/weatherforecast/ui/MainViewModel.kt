package com.awesomejim.weatherforecast.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.weatherforecast.data.SettingsRepository
import com.awesomejim.weatherforecast.data.WeatherRepository
import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.di.network.RetrialResult
import com.awesomejim.weatherforecast.ui.common.toResourceId
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
    private val defaultWeatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private lateinit var currentLocation: DefaultLocation
    private lateinit var preferredUnits: String

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
            ){ pUnits, defaultLocation-> 
                Pair(pUnits, defaultLocation)
            }.collect{ (pUnits, defaultLocation) ->
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
                is RetrialResult.Success -> {
                    val weatherData = result.data
                    Timber.e("weatherData result:: ${result.data}")
                    Timber.i("Size :: ${weatherData.size}")
                    val responseList = mutableListOf<LocationItemData>()
                    weatherData.forEach { (num, list) ->
                        responseList.add(list[0])
                    }
                    _forecastListState.emit(responseList)
                }

                is RetrialResult.Error -> {
                    Timber.e("Error :: ${result.errorType.toResourceId()}")
                }
            }
        }
    }


    fun processIntent(mainViewUiState: MainViewUiState) {
        Timber.tag("MainViewModel").e("processIntent called ${mainViewUiState.toString()}")
        when (mainViewUiState) {
            is MainViewUiState.GrantPermission -> {
                setState { copy(isPermissionGranted = mainViewUiState.isGranted) }
            }

            is MainViewUiState.CheckLocationSettings -> {
                setState { copy(isLocationSettingEnabled = mainViewUiState.isEnabled) }
            }

            is MainViewUiState.ReceiveLocation -> {
                val defaultLocation = DefaultLocation(
                    longitude = mainViewUiState.longitude,
                    latitude = mainViewUiState.latitude
                )
                this.currentLocation = defaultLocation
                Timber.tag("MainViewModel").e("defaultLocation ${defaultLocation.latitude}")
                setState { copy(defaultLocation = defaultLocation) }
            }
        }
    }

    private fun setState(stateReducer: MainViewState.() -> MainViewState) {
        viewModelScope.launch {
            _state.emit(stateReducer(state.value))
        }
    }


    private fun processCurrentWeatherResult(result: RetrialResult<LocationItemData>): CurrentWeatherUiState {
        return when (result) {
            is RetrialResult.Success -> {
                val weatherData = result.data
                Timber.e("weatherData result:: ${result.data}")
                Timber.i("locationName :: ${weatherData.locationName}")
                CurrentWeatherUiState.Success(result.data)
            }

            is RetrialResult.Error -> {
                Timber.e("Error :: ${result.errorType.toResourceId()}")
                CurrentWeatherUiState.Error(result.errorType.toResourceId())
            }
        }
    }


    fun testAPiCall() {
        if (this::currentLocation.isInitialized) {
            fetchCurrentWeatherData()
            fetchForecastCurrentWeatherData()
        } else {
            Timber.tag("MainViewModel").e("No Location Details")
        }
    }


}

data class MainViewState(
    val isPermissionGranted: Boolean = false,
    val isLocationSettingEnabled: Boolean = false,
    val defaultLocation: DefaultLocation? = null
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
