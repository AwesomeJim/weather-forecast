package com.awesomejim.weatherforecast.core.data.fake


import com.awesomejim.weatherforecast.core.LocationItemData
import com.awesomejim.weatherforecast.core.WeatherStatusInfo
import com.awesomejim.weatherforecast.core.network.model.CoordinatesResponse
import com.awesomejim.weatherforecast.core.network.model.CountryDetailsResponse
import com.awesomejim.weatherforecast.core.network.model.CurrentWeatherResponse
import com.awesomejim.weatherforecast.core.network.model.WeatherInfoResponse
import com.awesomejim.weatherforecast.core.network.model.WeatherItemResponse
import com.awesomejim.weatherforecast.core.network.model.WindResponse
import java.util.Date

object FakeResponseData {

    val fakeSuccessWeatherResponse =
        WeatherItemResponse(
            locationName = "Mountain View",
            locationId = 1695383,
            locationNameTimeZoneShift = -25200,
            forecastedTime = 1695455230,
            currentWeatherVisibility = 10000.0,
            countryDetails = CountryDetailsResponse(
                locationCountry = "US"
            ),
            currentWeatherMain = CurrentWeatherResponse(
                weatherTemp = 13.07f,
                weatherTempMin = 10.75f,
                weatherTempMax = 15.32f,
                weatherTempFeelsLike = 12.65f,
                weatherPressure = 1015.0,
                weatherHumidity = 85.0
            ),
            weatherConditionResponse = listOf(
                WeatherInfoResponse(
                    id = 804,
                    main = "Clouds",
                    description = "overcast clouds",
                    icon = "04n"
                )
            ),
            currentWeatherWind = WindResponse(
                speed = 36.37,
                degree = 38.39
            ),
            locationCoordinates = CoordinatesResponse(
                longitude = -122.084,
                latitude = 37.4234
            )

        )

    val fakeSuccessMappedWeatherResponse = LocationItemData(
        locationName = "Mountain View - US",
        locationId = 1695383,
        locationTimeZoneShift = -25200,
        locationDataTime = 1695455230,
        locationLongitude = -122.084,
        locationLatitude = 37.4234,
        locationWeatherInfo = WeatherStatusInfo(
            weatherConditionId = 804,
            weatherCondition = "Clouds",
            weatherConditionDescription = "overcast clouds",
            weatherConditionIcon = "04n",
            weatherTemp = 13.07f,
            weatherTempMin = 10.75f,
            weatherTempMax = 15.32f,
            weatherTempFeelsLike = 12.65f,
            weatherPressure = 1015.0,
            weatherHumidity = 85.0,
            weatherWindSpeed = 40.41,
            weatherWindDegrees = 38.39,
            weatherVisibility = 10000.0
        ),
        locationWeatherDay = 5,
        locationDataLastUpdate = Date()
    )
}
