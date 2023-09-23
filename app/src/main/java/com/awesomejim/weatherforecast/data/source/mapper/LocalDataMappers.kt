package com.awesomejim.weatherforecast.data.source.mapper

import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.data.model.WeatherStatusInfo
import com.awesomejim.weatherforecast.data.source.local.entity.LocationItemEntity
import com.awesomejim.weatherforecast.data.source.local.entity.WeatherStatusInfoEntity


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

fun LocationItemEntity.toLocationItem() = LocationItemData(
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