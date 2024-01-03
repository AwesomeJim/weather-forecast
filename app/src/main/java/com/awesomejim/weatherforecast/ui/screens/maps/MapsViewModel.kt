package com.awesomejim.weatherforecast.ui.screens.maps


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.weatherforecast.core.data.SettingsRepository
import com.awesomejim.weatherforecast.core.data.source.local.LocalDataSource
import com.awesomejim.weatherforecast.ui.screens.search.SavedLocationListUiState
import com.awesomejim.weatherforecast.ui.screens.search.SearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Awesome Jim on.
 * 03/10/2023
 */

@HiltViewModel
class MapsViewModel @Inject constructor(
    localDataSource: com.awesomejim.weatherforecast.core.data.source.local.LocalDataSource,
    private val settingsRepository: com.awesomejim.weatherforecast.core.data.SettingsRepository
) : ViewModel() {

     lateinit var currentLocation: com.awesomejim.weatherforecast.core.DefaultLocation

    init {
        viewModelScope.launch {
            currentLocation = settingsRepository.getDefaultLocation().first()
        }
    }

    val savedLocationListUiState: StateFlow<SavedLocationListUiState> =
        localDataSource.loadAllLocation().map { list ->
            if (list != null) {
                SavedLocationListUiState(list)
            } else {
                SavedLocationListUiState()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SearchViewModel.TIMEOUT_MILLIS),
            initialValue = SavedLocationListUiState()
        )
}
