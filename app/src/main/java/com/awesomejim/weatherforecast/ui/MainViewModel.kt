package com.awesomejim.weatherforecast.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.weatherforecast.data.DefaultWeatherRepository
import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.data.model.WeatherItemDataModel
import com.awesomejim.weatherforecast.di.network.RetrialResult
import com.awesomejim.weatherforecast.ui.common.toResourceId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val defaultWeatherRepository: DefaultWeatherRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainViewState())
    val state: StateFlow<MainViewState> = _state.asStateFlow()

    fun processIntent(mainViewUiState: MainViewUiState) {
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
                 viewModelScope.launch {
                     Timber.e("fetchWeatherDataWithCoordinates :: ${defaultLocation.latitude}")

                    val result = defaultWeatherRepository.fetchWeatherDataWithCoordinates(
                         defaultLocation = defaultLocation, units = "metric"
                     )
                     Log.e("TAG", "fetchWeatherDataWithCoordinates result:: ${result.toString()}")
                     processResult(result)
                 }
               // setState { copy(defaultLocation = defaultLocation) }
            }
        }
    }

    private fun setState(stateReducer: MainViewState.() -> MainViewState) {
        viewModelScope.launch {
            _state.emit(stateReducer(state.value))
        }
    }


    private fun processResult(result: RetrialResult<WeatherItemDataModel>) {
        when (result) {
            is RetrialResult.Success -> {
                val weatherData = result.data
                Timber.i("weatherData :: ${weatherData.locationName}")
            }

            is RetrialResult.Error -> {
                Timber.e("Error :: ${result.errorType.toResourceId()}")
            }
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