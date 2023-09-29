package com.awesomejim.weatherforecast.di

import com.awesomejim.weatherforecast.di.network.ForecastResponse
import com.awesomejim.weatherforecast.di.network.NetworkUtils.APP_ID_PARAM
import com.awesomejim.weatherforecast.di.network.NetworkUtils.LAT_PARAM
import com.awesomejim.weatherforecast.di.network.NetworkUtils.LON_PARAM
import com.awesomejim.weatherforecast.di.network.NetworkUtils.QUERY_PARAM
import com.awesomejim.weatherforecast.di.network.NetworkUtils.UNITS_PARAM
import com.awesomejim.weatherforecast.di.network.WeatherItemResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/weather?")
    suspend fun fetchWeatherDataWithCoordinates(
        @Query(APP_ID_PARAM) appid: String,
        @Query(UNITS_PARAM) units: String,
        @Query(LAT_PARAM) latitude: Double,
        @Query(LON_PARAM) longitude: Double
    ): Response<WeatherItemResponse>

    @GET("data/2.5/weather?")
    suspend fun fetchWeatherWithLocationQuery(
        @Query(APP_ID_PARAM) appid: String,
        @Query(UNITS_PARAM) units: String,
        @Query(QUERY_PARAM) locationQuery: String
    ): Response<WeatherItemResponse>

    @GET("data/2.5/forecast?")
    suspend fun fetchWeatherForecast(
        @Query(APP_ID_PARAM) appid: String,
        @Query(UNITS_PARAM) units: String,
        @Query(LAT_PARAM) latitude: Double,
        @Query(LON_PARAM) longitude: Double
    ): Response<ForecastResponse>
}
