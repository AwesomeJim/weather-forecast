package com.awesomejim.weatherforecast.data

import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.di.network.RetrialResult


interface WeatherRepository {

    suspend fun fetchWeatherDataWithCoordinates(
        defaultLocation: DefaultLocation,
        units: String
    ) : RetrialResult<LocationItemData>


    suspend fun fetchWeatherDataWithLocationQuery(
        locationQuery: String,
        units: String
    ): RetrialResult<LocationItemData>



    suspend fun fetchWeatherForecastWithCoordinates(
        defaultLocation: DefaultLocation,
        units: String
    ): RetrialResult<Map<Int, List<LocationItemData>>>
}
