package com.awesomejim.weatherforecast.core.data

import com.awesomejim.weatherforecast.core.DefaultLocation
import com.awesomejim.weatherforecast.core.LocationItemData
import com.awesomejim.weatherforecast.core.data.utils.RetrialResult


interface WeatherRepository {

    suspend fun fetchWeatherDataWithCoordinates(
        defaultLocation: DefaultLocation,
        units: String
    ): RetrialResult<LocationItemData>

    suspend fun fetchWeatherDataWithLocationQuery(
        locationQuery: String,
        units: String
    ): RetrialResult<LocationItemData>

    suspend fun fetchWeatherForecastWithCoordinates(
        defaultLocation: DefaultLocation,
        units: String
    ): RetrialResult<List<LocationItemData>>
}
