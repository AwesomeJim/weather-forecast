package com.awesomejim.weatherforecast.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    @SerialName("city") val foreCastCity: ForecastCityResponse,
    @SerialName("list") val foreCastList: List<ForecastLisResponse>
)

@Serializable
data class ForecastCityResponse(
    @SerialName("name") val locationName: String,
    @SerialName("id") val locationId: Long,
    @SerialName("timezone") val locationNameTimeZoneShift: Long,
    @SerialName("country") val locationCountry: String,
    @SerialName("coord") val locationCoordinates: CoordinatesResponse,
)

@Serializable
data class ForecastLisResponse(
    @SerialName("dt") val forecastedTime: Long,
    @SerialName("visibility") val currentWeatherVisibility: Double,
    @SerialName("main") val currentWeatherMain: CurrentWeatherResponse,
    @SerialName("weather") val weatherConditionResponse: List<WeatherInfoResponse>,
    @SerialName("wind") val currentWeatherWind: WindResponse
)

@Serializable
data class WeatherItemResponse(
    @SerialName("name") val locationName: String,
    @SerialName("id") val locationId: Long,
    @SerialName("timezone") val locationNameTimeZoneShift: Long,
    @SerialName("dt") val forecastedTime: Long,
    @SerialName("visibility") val currentWeatherVisibility: Double,
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
    @SerialName("temp") val weatherTemp: Float,
    @SerialName("feels_like") val weatherTempFeelsLike: Float,
    @SerialName("temp_min") val weatherTempMin: Float,
    @SerialName("temp_max") val weatherTempMax: Float,
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
