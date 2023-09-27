package com.awesomejim.weatherforecast.ui.screens.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.awesomejim.weatherforecast.data.SettingsRepository
import com.awesomejim.weatherforecast.data.flickr.FlickrRepository
import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.di.flickr.FlickerPhotoResponse
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

    fun getLocationPhotos(location: DefaultLocation): Flow<PagingData<FlickerPhotoResponse>> =
        repository
            .getPhotos(currentLocation)
            .cachedIn(viewModelScope)
}