package com.awesomejim.weatherforecast.data

import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.di.network.RetrialResult
import kotlinx.coroutines.flow.Flow


interface WeatherRepository {

    suspend fun fetchWeatherDataWithCoordinates(
        defaultLocation: DefaultLocation,
        units: String
    ) : Flow<RetrialResult<LocationItemData>>


    suspend fun fetchWeatherDataWithLocationQuery(
        locationQuery: String,
        units: String
    ): Flow<RetrialResult<LocationItemData>>



    suspend fun fetchWeatherForecastWithCoordinates(
        defaultLocation: DefaultLocation,
        units: String
    ): Flow<RetrialResult<Map<Int, List<LocationItemData>>>>
}
