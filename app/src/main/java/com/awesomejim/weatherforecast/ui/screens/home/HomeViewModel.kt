package com.awesomejim.weatherforecast.ui.screens.home

import androidx.lifecycle.ViewModel
import com.awesomejim.weatherforecast.core.data.SettingsRepository
import com.awesomejim.weatherforecast.core.data.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val defaultWeatherRepository: com.awesomejim.weatherforecast.core.data.WeatherRepository,
    private val settingsRepository: com.awesomejim.weatherforecast.core.data.SettingsRepository
) : ViewModel()
