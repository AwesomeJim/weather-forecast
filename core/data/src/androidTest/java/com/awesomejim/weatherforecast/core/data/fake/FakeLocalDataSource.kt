package com.awesomejim.weatherforecast.core.data.fake

import com.awesomejim.weatherforecast.core.LocationItemData
import com.awesomejim.weatherforecast.core.WeatherStatusInfo
import java.util.Date

object FakeLocalDataSource {
    val locationList = listOf(
        LocationItemData(
            locationName = "Mountain View - US",
            locationId = 1695383,
            locationTimeZoneShift = -25200,
            locationDataTime = 1695455230,
            locationLongitude = -122.084,
            locationLatitude = 37.4234,
            locationWeatherInfo = WeatherStatusInfo(
                weatherConditionId = 804,
                weatherCondition = "Clouds",
                weatherConditionDescription = "overcast clouds", weatherConditionIcon = "04n",
                weatherTemp = 13.07f,
                weatherTempMin = 10.75f,
                weatherTempMax = 15.32f,
                weatherTempFeelsLike = 12.65f,
                weatherPressure = 1015.0,
                weatherHumidity = 85.0,
                weatherWindSpeed = 0.0,
                weatherWindDegrees = 0.0,
                weatherVisibility = 10000.0
            ),
            locationWeatherDay = 5,
            locationDataLastUpdate = Date()
        ),
        LocationItemData(
            locationName = "Nairobi View - US",
            locationId = 184745,
            locationTimeZoneShift = -25200,
            locationDataTime = 1695455230,
            locationLongitude = -122.084,
            locationLatitude = 37.4234,
            locationWeatherInfo = WeatherStatusInfo(
                weatherConditionId = 804,
                weatherCondition = "Clouds",
                weatherConditionDescription = "overcast clouds", weatherConditionIcon = "04n",
                weatherTemp = 13.07f,
                weatherTempMin = 10.75f,
                weatherTempMax = 15.32f,
                weatherTempFeelsLike = 12.65f,
                weatherPressure = 1015.0,
                weatherHumidity = 85.0,
                weatherWindSpeed = 0.0,
                weatherWindDegrees = 0.0,
                weatherVisibility = 10000.0
            ),
            locationWeatherDay = 6,
            locationDataLastUpdate = Date()
        ),
       LocationItemData(
            locationName = "Kisumu- KE",
            locationId = 5375480,
            locationTimeZoneShift = -25200,
            locationDataTime = 1695455230,
            locationLongitude = -122.084,
            locationLatitude = 37.4234,
            locationWeatherInfo = WeatherStatusInfo(
                weatherConditionId = 804,
                weatherCondition = "Clouds",
                weatherConditionDescription = "Overcast clouds", weatherConditionIcon = "04n",
                weatherTemp = 13.07f,
                weatherTempMin = 10.75f,
                weatherTempMax = 15.32f,
                weatherTempFeelsLike = 12.65f,
                weatherPressure = 1015.0,
                weatherHumidity = 85.0,
                weatherWindSpeed = 0.0,
                weatherWindDegrees = 0.0,
                weatherVisibility = 10000.0
            ),
            locationWeatherDay = 24,
            locationDataLastUpdate = Date()
        )
    )
}
