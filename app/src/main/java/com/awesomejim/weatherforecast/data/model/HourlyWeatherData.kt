package com.awesomejim.weatherforecast.data.model

import androidx.annotation.DrawableRes

data class ForecastMoreDetails(
    val windDetails: String,
    val humidityDetails: String,
    val visibilityDetails: String,
    val pressureDetails: String,
    val hourlyWeatherData: List<HourlyWeatherData>
)

data class HourlyWeatherData(
    val temperature: String,
    @DrawableRes val drawableIcon: Int,
    val hourTime: String
)
