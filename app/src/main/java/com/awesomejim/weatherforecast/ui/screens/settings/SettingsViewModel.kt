package com.awesomejim.weatherforecast.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.weatherforecast.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsScreenViewState())
    val state: StateFlow<SettingsScreenViewState> = _state.asStateFlow()

    fun processSettingsScreenUiState(settingsScreenUiState: SettingsScreenUiState) {
        when (settingsScreenUiState) {
            SettingsScreenUiState.LoadSettingScreenData -> {
                viewModelScope.launch {
                    settingsRepository.getUnits().collect { units ->
                        setState {
                            copy(
                                selectedUnit = units,
                                versionInfo = settingsRepository.getAppVersion(),
                                availableUnits = settingsRepository.getAvailableUnits()
                            )
                        }
                    }
                }
            }

            is SettingsScreenUiState.ChangeUnits -> {
                viewModelScope.launch {
                    settingsRepository.setUnits(settingsScreenUiState.selectedUnits)
                    setState { copy(selectedUnit = settingsScreenUiState.selectedUnits) }
                }
            }
        }
    }

    private fun setState(stateReducer: SettingsScreenViewState.() -> SettingsScreenViewState) {
        viewModelScope.launch {
            _state.emit(stateReducer(state.value))
        }
    }
}

data class SettingsScreenViewState(
    val selectedUnit: String = "",
    val availableUnits: List<String> = emptyList(),
    val versionInfo: String = "",
    val error: Throwable? = null
)

sealed class SettingsScreenUiState {

    object LoadSettingScreenData : SettingsScreenUiState()

    data class ChangeUnits(val selectedUnits: String) : SettingsScreenUiState()
}
