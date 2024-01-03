package com.awesomejim.weatherforecast.core.data.source.remote

import com.awesomejim.weatherforecast.core.DefaultLocation
import com.awesomejim.weatherforecast.core.LocationItemData
import com.awesomejim.weatherforecast.core.network.RetrialResult

interface RemoteDataSource {

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
