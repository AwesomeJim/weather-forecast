package com.awesomejim.weatherforecast.data.model


data class WeatherItemDataModel(
    val locationName: String,
    val locationId: Long,
    val locationNameTimeZoneShift: Long,
    var locationDate: Long,
    val locationCoordinates: Coordinates,
    val locationWeather: WeatherStatus,
    val locationWeatherDay: Int
)

data class Coordinates(
    val longitude: Double,
    val latitude: Double,
)

data class WeatherStatus(
    val weatherConditionId: Int,
    val weatherCondition: String,
    val weatherConditionDescription: String,
    val weatherConditionIcon: String,
    val weatherTemp: String,
    val weatherTempMin: String,
    val weatherTempMax: String,
    val weatherPressure: Double,
    val weatherHumidity: Double,
    val weatherWind: Wind
)
data class Wind(
    val speed: Double,
    val deg: Double,
)