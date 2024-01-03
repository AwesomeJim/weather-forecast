package com.awesomejim.weatherforecast.ui.screens.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.awesomejim.weatherforecast.core.data.SettingsRepository
import com.awesomejim.weatherforecast.core.data.flickr.FlickrRepository
import com.awesomejim.weatherforecast.core.network.flickr.FlickerPhotoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val repository: com.awesomejim.weatherforecast.core.data.flickr.FlickrRepository,
    private val settingsRepository: com.awesomejim.weatherforecast.core.data.SettingsRepository
) : ViewModel() {

    private lateinit var currentLocation: com.awesomejim.weatherforecast.core.DefaultLocation

    init {
        viewModelScope.launch {
            settingsRepository.getDefaultLocation().collect { defaultLocation ->
                currentLocation = defaultLocation
            }
        }
    }

    fun getLocationPhotos(location: com.awesomejim.weatherforecast.core.DefaultLocation): Flow<PagingData<com.awesomejim.weatherforecast.core.network.flickr.FlickerPhotoResponse>> =
        repository
            .getPhotos(location)
            .cachedIn(viewModelScope)
}
