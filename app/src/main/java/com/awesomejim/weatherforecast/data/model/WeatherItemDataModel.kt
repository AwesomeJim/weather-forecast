package com.awesomejim.weatherforecast.data.model


data class WeatherItemDataModel(
    val locationName: String,
    val locationId: Int,
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
    val weatherTemp: Double,
    val weatherTempMin: Double,
    val weatherTempMax: Double,
    val weatherPressure: Double,
    val weatherHumidity: Int,
    val weatherWind: Wind
)
data class Wind(
    val speed: Double,
    val deg: Double,
)