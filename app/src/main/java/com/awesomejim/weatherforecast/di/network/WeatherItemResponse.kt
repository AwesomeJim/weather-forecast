package com.awesomejim.weatherforecast.di.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WeatherItemResponse(
    @SerialName("name") val locationName: String,
    @SerialName("id") val locationNameId: Long,
    @SerialName("timezone") val locationNameTimeZoneShift: Long,
    @SerialName("dt") val forecastedTime: Long,
    @SerialName("sys") val countryDetails: CountryDetailsResponse,
    @SerialName("main") val currentWeatherMain: CurrentWeatherResponse,
    @SerialName("weather") val weatherConditionResponse: List<WeatherInfoResponse>,
    @SerialName("wind") val currentWeatherWind: WindResponse,
    @SerialName("coord") val locationCoordinates: CoordinatesResponse
)

@Serializable
data class CountryDetailsResponse(
    @SerialName("country") val locationCountry: String,
)


@Serializable
data class CurrentWeatherResponse(
    @SerialName("temp") val weatherTemp: Double,
    @SerialName("feels_like") val weatherTempFeelsLike: Double,
    @SerialName("temp_min") val weatherTempMin: Double,
    @SerialName("temp_max") val weatherTempMax: Double,
    @SerialName("pressure") val weatherPressure: Double,
    @SerialName("humidity") val weatherHumidity: Double
)

@Serializable
data class CoordinatesResponse(
    @SerialName("lon") val longitude: Double,
    @SerialName("lat") val latitude: Double,
)

@Serializable
data class WindResponse(
    @SerialName("speed") val speed: Double,
    @SerialName("deg") val degree: Double,
)

@Serializable
data class WeatherInfoResponse(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class TemperatureResponse(
    @SerialName("min") val min: Float,
    @SerialName("max") val max: Float,
)