package com.awesomejim.weatherforecast.data.source.mapper

import com.awesomejim.weatherforecast.data.model.ForecastMoreDetails
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.data.model.WeatherStatusInfo
import com.awesomejim.weatherforecast.data.source.local.entity.LocationItemEntity
import com.awesomejim.weatherforecast.data.source.local.entity.WeatherStatusInfoEntity
import com.awesomejim.weatherforecast.di.network.getFormattedWind

fun LocationItemData.toLocationEntity() = LocationItemEntity(
    locationId = locationId,
    locationName = locationName,
    locationTimeZoneShift = locationTimeZoneShift,
    locationDataTime = locationDataTime,
    locationLongitude = locationLongitude,
    locationLatitude = locationLatitude,
    locationWeatherDay = locationWeatherDay,
    locationDataLastUpdate = locationDataLastUpdate,
    isFavorite = false,
    locationWeatherInfo = WeatherStatusInfoEntity(
        weatherConditionId = locationWeatherInfo.weatherConditionId,
        weatherCondition = locationWeatherInfo.weatherCondition,
        weatherConditionDescription = locationWeatherInfo.weatherConditionDescription,
        weatherConditionIcon = locationWeatherInfo.weatherConditionIcon,
        weatherTemp = locationWeatherInfo.weatherTemp,
        weatherTempMin = locationWeatherInfo.weatherTempMin,
        weatherTempMax = locationWeatherInfo.weatherTempMax,
        weatherTempFeelsLike = locationWeatherInfo.weatherTempFeelsLike,
        weatherPressure = locationWeatherInfo.weatherPressure,
        weatherHumidity = locationWeatherInfo.weatherHumidity,
        weatherWindSpeed = locationWeatherInfo.weatherWindSpeed,
        weatherWindDegrees = locationWeatherInfo.weatherWindDegrees,
        weatherVisibility = locationWeatherInfo.weatherVisibility
    )
)

fun LocationItemEntity.toLocationItem(addSummary: Boolean = true): LocationItemData {
    val locationItemData = LocationItemData(
        locationName = locationName,
        locationId = locationId,
        locationTimeZoneShift = locationTimeZoneShift,
        locationDataTime = locationDataTime,
        locationLongitude = locationLongitude,
        locationLatitude = locationLatitude,
        locationWeatherInfo = WeatherStatusInfo(
            weatherConditionId = locationWeatherInfo.weatherConditionId,
            weatherCondition = locationWeatherInfo.weatherCondition,
            weatherConditionDescription = locationWeatherInfo.weatherConditionDescription,
            weatherConditionIcon = locationWeatherInfo.weatherConditionIcon,
            weatherTemp = locationWeatherInfo.weatherTemp,
            weatherTempMin = locationWeatherInfo.weatherTempMin,
            weatherTempMax = locationWeatherInfo.weatherTempMax,
            weatherTempFeelsLike = locationWeatherInfo.weatherTempFeelsLike,
            weatherPressure = locationWeatherInfo.weatherPressure,
            weatherHumidity = locationWeatherInfo.weatherHumidity,
            weatherWindSpeed = locationWeatherInfo.weatherWindSpeed,
            weatherWindDegrees = locationWeatherInfo.weatherWindDegrees,
            weatherVisibility = locationWeatherInfo.weatherVisibility
        ),
        locationWeatherDay = locationWeatherDay,
        locationDataLastUpdate = locationDataLastUpdate
    )
    if (addSummary) {
        val windDirection =
            getFormattedWind(locationItemData.locationWeatherInfo.weatherWindDegrees)
        val visibility = locationItemData.locationWeatherInfo.weatherVisibility / 1000
        val forecastMoreDetails = ForecastMoreDetails(
            windDetails = "%1\$1.0f km/h %2\$s".format(
                locationItemData.locationWeatherInfo.weatherWindSpeed.toFloat(),
                windDirection
            ),
            humidityDetails = "%1.0f %%"
                .format(locationItemData.locationWeatherInfo.weatherHumidity.toFloat()),
            visibilityDetails = "$visibility km",
            pressureDetails = "%1.0f hPa"
                .format(locationItemData.locationWeatherInfo.weatherPressure.toFloat()),
            hourlyWeatherData = listOf()
        )

        locationItemData.forecastMoreDetails = forecastMoreDetails
    }
    return locationItemData
}
