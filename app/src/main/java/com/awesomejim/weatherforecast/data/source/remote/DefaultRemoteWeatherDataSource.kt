package com.awesomejim.weatherforecast.data.source.remote

import com.awesomejim.weatherforecast.BuildConfig
import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.di.ApiService
import com.awesomejim.weatherforecast.di.network.RetrivalResult
import com.awesomejim.weatherforecast.di.network.WeatherItemResponse
import com.awesomejim.weatherforecast.di.network.mapResponseCodeToThrowable
import javax.inject.Inject

class DefaultRemoteWeatherDataSource @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {
    override suspend fun fetchWeatherDataWithCoordinates(
        defaultLocation: DefaultLocation,
        units: String
    ): RetrivalResult<WeatherItemResponse> {
        TODO()
    }



    override suspend fun fetchWeatherDataWithLocationQuery(
        locationQuery: String,
        units: String
    ): RetrivalResult<WeatherItemResponse> {
        TODO("Not yet implemented")
    }
}