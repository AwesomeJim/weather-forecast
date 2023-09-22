package com.awesomejim.weatherforecast.di



import com.awesomejim.weatherforecast.di.network.NetworkUtils.APP_ID_PARAM
import com.awesomejim.weatherforecast.di.network.NetworkUtils.LAT_PARAM
import com.awesomejim.weatherforecast.di.network.NetworkUtils.LON_PARAM
import com.awesomejim.weatherforecast.di.network.NetworkUtils.QUERY_PARAM
import com.awesomejim.weatherforecast.di.network.NetworkUtils.UNITS_PARAM
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("weather?")
    suspend fun fetchWeatherWithLatitudeLongitude(
        @Query(APP_ID_PARAM) appid: String,
        @Query(UNITS_PARAM) units: String,
        @Query(LAT_PARAM) lat: String,
        @Query(LON_PARAM) lon: String
    ): Response<String>


    @GET("weather?")
    suspend fun fetchWeatherWithLocationQuery(
        @Query(APP_ID_PARAM) appid: String,
        @Query(UNITS_PARAM) units: String,
        @Query(QUERY_PARAM) locationQuery: String
    ): Response<String>


    @GET("forecast?")
    suspend fun fetchWeatherForecast(
        @Query(APP_ID_PARAM) appid: String,
        @Query(UNITS_PARAM) units: String,
        @Query(LAT_PARAM) lat: String,
        @Query(LON_PARAM) lon: String
    ): Response<String>

}