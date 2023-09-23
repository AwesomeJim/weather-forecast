package com.awesomejim.weatherforecast.data.source.remote

import com.awesomejim.weatherforecast.BuildConfig
import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.di.ApiService
import com.awesomejim.weatherforecast.di.network.RetrialResult
import com.awesomejim.weatherforecast.di.network.mapResponseCodeToThrowable
import com.awesomejim.weatherforecast.di.network.toCoreModel
import timber.log.Timber
import javax.inject.Inject

class DefaultRemoteWeatherDataSource @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {
    override suspend fun fetchWeatherDataWithCoordinates(
        defaultLocation: DefaultLocation,
        units: String
    ): RetrialResult<LocationItemData> =
            try {
                val response = apiService.fetchWeatherDataWithCoordinates(
                    appid = BuildConfig.OPEN_WEATHER_APP_ID,
                    units = units,
                    latitude = defaultLocation.latitude,
                    longitude = defaultLocation.longitude
                )
                if (response.isSuccessful && response.body() != null) {
                    val weatherData = response.body()!!.toCoreModel()
                    RetrialResult.Success(data = weatherData)
                } else {
                    val throwable = mapResponseCodeToThrowable(response.code())
                    throw throwable
                }
            } catch (e: Exception) {
                Timber.e(
                    "<<<<<<<<<fetchWeatherDataWithCoordinates Exception>>>>>>>>>>: %s",
                    e.message
                )
                throw e
            }


    override suspend fun fetchWeatherDataWithLocationQuery(
        locationQuery: String,
        units: String
    ): RetrialResult<LocationItemData> =
            try {
                val response = apiService.fetchWeatherWithLocationQuery(
                    appid = BuildConfig.OPEN_WEATHER_APP_ID,
                    units = units,
                    locationQuery = locationQuery
                )
                if (response.isSuccessful && response.body() != null) {
                    val weatherData = response.body()!!.toCoreModel()
                    RetrialResult.Success(data = weatherData)
                } else {
                    val throwable = mapResponseCodeToThrowable(response.code())
                    throw throwable
                }
            } catch (e: Exception) {
                Timber.e(
                    "<<<<<<<<<fetchWeatherDataWithCoordinates Exception>>>>>>>>>>: %s",
                    e.message
                )
                throw e
            }
}