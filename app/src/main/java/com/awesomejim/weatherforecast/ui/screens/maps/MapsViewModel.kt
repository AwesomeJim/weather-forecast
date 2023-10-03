/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.awesomejim.weatherforecast.ui.screens.maps


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.weatherforecast.data.source.local.LocalDataSource
import com.awesomejim.weatherforecast.ui.screens.search.SavedLocationListUiState
import com.awesomejim.weatherforecast.ui.screens.search.SearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    localDataSource: LocalDataSource
) : ViewModel() {


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
