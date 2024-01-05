package com.awesomejim.weatherforecast.feature.forecast

import androidx.lifecycle.ViewModel
import com.awesomejim.weatherforecast.core.data.SettingsRepository
import com.awesomejim.weatherforecast.core.data.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class ForecastHomeViewModel @Inject constructor(
    private val defaultWeatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel()
