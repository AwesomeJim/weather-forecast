package com.awesomejim.weatherforecast.data.source.remote

import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.di.network.RetrivalResult
import com.awesomejim.weatherforecast.di.network.WeatherItemResponse

interface RemoteDataSource {

    suspend fun fetchWeatherDataWithCoordinates(
        defaultLocation: DefaultLocation,
        units: String
    ): RetrivalResult<WeatherItemResponse>


    suspend fun fetchWeatherDataWithLocationQuery(
        locationQuery: String,
        units: String
    ): RetrivalResult<WeatherItemResponse>
}