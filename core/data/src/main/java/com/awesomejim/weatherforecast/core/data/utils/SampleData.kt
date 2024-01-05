package com.awesomejim.weatherforecast.core.data.utils


import com.awesomejim.weatherforecast.core.ForecastMoreDetailsItem
import com.awesomejim.weatherforecast.core.HourlyWeatherData
import com.awesomejim.weatherforecast.core.LocationItemData
import com.awesomejim.weatherforecast.core.WeatherStatusInfo
import com.awesomejim.weatherforecast.core.data.R
import java.util.Date

object SampleData {

    val hourlyWeatherDataList = listOf(
        HourlyWeatherData(
            temperature = "23\u00B0",
            drawableIcon = R.drawable.art_light_rain,
            hourTime = "16:00"

        ),
        HourlyWeatherData(
            temperature = "23\u00B0",
            drawableIcon = R.drawable.art_storm,
            hourTime = "17:00"

        ),
        HourlyWeatherData(
            temperature = "15\u00B0",
            drawableIcon = R.drawable.art_snow,
            hourTime = "18:00"

        ),
        HourlyWeatherData(
            temperature = "14\u00B0",
            drawableIcon = R.drawable.art_clouds,
            hourTime = "19:00"

        ),
        HourlyWeatherData(
            temperature = "23\u00B0",
            drawableIcon = R.drawable.art_light_rain,
            hourTime = "16:00"

        ),
        HourlyWeatherData(
            temperature = "23\u00B0",
            drawableIcon = R.drawable.art_storm,
            hourTime = "17:00"

        ),
        HourlyWeatherData(
            temperature = "15\u00B0",
            drawableIcon = R.drawable.art_snow,
            hourTime = "18:00"

        ),
        HourlyWeatherData(
            temperature = "14\u00B0",
            drawableIcon = R.drawable.art_clouds,
            hourTime = "19:00"

        )
    )

    val forecastMoreDetailsItem = ForecastMoreDetailsItem(
        windDetails = "dolore",
        humidityDetails = "malorum",
        visibilityDetails = "libero",
        pressureDetails = "eloquentiam",
        hourlyWeatherData = hourlyWeatherDataList
    )

    val sampleLocationItemData = LocationItemData(
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
        forecastMoreDetailsItem = forecastMoreDetailsItem,
        locationDataLastUpdate = Date()
    )

    val foreCastList = listOf(
        LocationItemData(
            locationName = "Mountain View - US",
            locationId = 1695383,
            locationTimeZoneShift = -25200,
            locationDataTime = 1695643200,
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
        ),
        LocationItemData(
            locationName = "Kenya- US",
            locationId = 16925383,
            locationTimeZoneShift = -252200,
            locationDataTime = 1695578400,
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
        ),
        LocationItemData(
            locationName = "Fern Herring",
            locationId = 7088,
            locationTimeZoneShift = 7333,
            locationDataTime = 1695837600,
            locationLongitude = 132.133,
            locationLatitude = 134.135,
            locationWeatherInfo = WeatherStatusInfo(
                weatherConditionId = 9073,
                weatherCondition = "nullam",
                weatherConditionDescription = "falli",
                weatherConditionIcon = "affert",
                weatherTemp = 136.137f,
                weatherTempMin = 138.139f,
                weatherTempMax = 140.141f,
                weatherTempFeelsLike = 142.143f,
                weatherPressure = 144.145,
                weatherHumidity = 146.147,
                weatherWindSpeed = 148.149,
                weatherWindDegrees = 150.151,
                weatherVisibility = 152.153
            ),
            locationWeatherDay = 4223,
            locationDataLastUpdate = Date()
        )
    )
}
