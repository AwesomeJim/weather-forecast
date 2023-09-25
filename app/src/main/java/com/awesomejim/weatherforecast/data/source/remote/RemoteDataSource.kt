package com.awesomejim.weatherforecast.data.source.remote

import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.di.network.RetrialResult

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