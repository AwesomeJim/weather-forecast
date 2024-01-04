package com.awesomejim.weatherforecast.core

import androidx.annotation.DrawableRes

data class ForecastMoreDetailsItem(
    val windDetails: String,
    val humidityDetails: String,
    val visibilityDetails: String,
    val pressureDetails: String,
    val hourlyWeatherData: List<HourlyWeatherData>
)

data class HourlyWeatherData(
    val temperature: String,
    @DrawableRes val drawableIcon: Int,
    val hourTime: String,
    var temperatureFloat: Float = 0.0f,
)
