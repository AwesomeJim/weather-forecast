package com.awesomejim.weatherforecast.core

import java.util.Date

data class LocationItemData(
    val locationName: String,
    var locationId: Long,
    val locationTimeZoneShift: Long,
    var locationDataTime: Long,
    val locationLongitude: Double,
    val locationLatitude: Double,
    val locationWeatherInfo: WeatherStatusInfo,
    val locationWeatherDay: Int,
    val locationDataLastUpdate: Date,
    var forecastMoreDetails: ForecastMoreDetails? = null
)

data class WeatherStatusInfo(
    val weatherConditionId: Int,
    val weatherCondition: String,
    val weatherConditionDescription: String,
    val weatherConditionIcon: String,
    val weatherTemp: Float,
    val weatherTempMin: Float,
    val weatherTempMax: Float,
    val weatherTempFeelsLike: Float,
    val weatherPressure: Double,
    val weatherHumidity: Double,
    val weatherWindSpeed: Double,
    val weatherWindDegrees: Double,
    val weatherVisibility: Double
)
