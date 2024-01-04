package com.awesomejim.weatherforecast.feature.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.awesomejim.weatherforecast.core.DefaultLocation
import com.awesomejim.weatherforecast.core.FlickerPhoto
import com.awesomejim.weatherforecast.core.data.SettingsRepository
import com.awesomejim.weatherforecast.core.data.flickr.FlickrRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val repository: FlickrRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private lateinit var currentLocation: DefaultLocation

    init {
        viewModelScope.launch {
            settingsRepository.getDefaultLocation().collect { defaultLocation ->
                currentLocation = defaultLocation
            }
        }
    }

    fun getLocationPhotos(location: DefaultLocation): Flow<PagingData<FlickerPhoto>> =
        repository
            .getPhotos(location)
            .cachedIn(viewModelScope)
}
