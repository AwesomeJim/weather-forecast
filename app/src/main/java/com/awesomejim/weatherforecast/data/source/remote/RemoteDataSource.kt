package com.awesomejim.weatherforecast.data.source.remote

import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.data.model.WeatherItemDataModel
import com.awesomejim.weatherforecast.di.network.RetrialResult

interface RemoteDataSource {

    suspend fun fetchWeatherDataWithCoordinates(
        defaultLocation: DefaultLocation,
        units: String
    ): RetrialResult<WeatherItemDataModel>


    suspend fun fetchWeatherDataWithLocationQuery(
        locationQuery: String,
        units: String
    ): RetrialResult<WeatherItemDataModel>
}