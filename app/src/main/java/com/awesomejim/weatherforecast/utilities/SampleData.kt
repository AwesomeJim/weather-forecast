package com.awesomejim.weatherforecast.utilities

import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.data.model.WeatherStatusInfo
import java.util.Date

object SampleData {
    val locationItemData = LocationItemData(
        locationName = "Terra Clarke",
        locationId = 2101,
        locationTimeZoneShift = 3546,
        locationDataTime = 4033,
        locationLongitude = 66.67,
        locationLatitude = 68.69,
        locationWeatherInfo = WeatherStatusInfo(
            weatherConditionId = 4395,
            weatherCondition = "dolore",
            weatherConditionDescription = "fringilla",
            weatherConditionIcon = "convenire",
            weatherTemp = 20.71f,
            weatherTempMin = 12.73f,
            weatherTempMax = 24.75f,
            weatherTempFeelsLike = 76.77f,
            weatherPressure = 78.79,
            weatherHumidity = 80.81,
            weatherWindSpeed = 82.83,
            weatherWindDegrees = 84.85,
            weatherVisibility = 86.87
        ),
        locationWeatherDay = 5771,
        locationDataLastUpdate = Date()
    )
}