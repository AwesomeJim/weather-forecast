package com.awesomejim.weatherforecast.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.weatherforecast.data.DefaultWeatherRepository
import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.di.network.RetrialResult
import com.awesomejim.weatherforecast.ui.common.toResourceId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val defaultWeatherRepository: DefaultWeatherRepository
) : ViewModel() {

    private lateinit var defaultLocation: DefaultLocation

    private val _state = MutableStateFlow(MainViewState())
    val state: StateFlow<MainViewState> = _state.asStateFlow()


    private val _currentWeatherUiState =
        MutableStateFlow<CurrentWeatherUiState>(CurrentWeatherUiState.Loading)

    val currentWeatherUiState: StateFlow<CurrentWeatherUiState> =
        _currentWeatherUiState.asStateFlow()


    /** The mutable State that stores the status of the most recent request */
    fun fetchCurrentWeatherData() {
        Timber.tag("MainViewModel").e("fetchCurrentWeatherData called")
        viewModelScope.launch {
            defaultWeatherRepository.fetchWeatherDataWithCoordinates(
                defaultLocation = defaultLocation, units = "metric"
            ).map {
                processCurrentWeatherResult(it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = CurrentWeatherUiState.Loading
            ).collect {
                Timber.tag("MainViewModel").e("fetchCurrentWeatherData called ${it.toString()}")
                _currentWeatherUiState.emit(it)
            }

        }
    }

    fun fetchForecastCurrentWeatherData() {
        viewModelScope.launch {
            Timber.tag("MainViewModel")
                .e("fetchWeatherDataWithCoordinates :: %s", defaultLocation.latitude)
            val result = defaultWeatherRepository.fetchWeatherForecastWithCoordinates(
                defaultLocation = defaultLocation, units = "metric",
            )
            Timber.e("fetchWeatherDataWithCoordinates result:: $result")
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
                this.defaultLocation = defaultLocation
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

    private fun processForecastResult(result: RetrialResult<Map<Int, List<LocationItemData>>>) {
        when (result) {
            is RetrialResult.Success -> {
                val weatherData = result.data
                Timber.e("weatherData result:: ${result.data}")
                Timber.i("Size :: ${weatherData.size}")
            }

            is RetrialResult.Error -> {
                Timber.e("Error :: ${result.errorType.toResourceId()}")
            }
        }
    }

    fun testAPiCall() {
        if (this::defaultLocation.isInitialized) {
            fetchCurrentWeatherData()
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
