package com.awesomejim.weatherforecast.core.data.source.remote

import com.awesomejim.weatherforecast.core.LocationItemData
import com.awesomejim.weatherforecast.core.data.BuildConfig
import com.awesomejim.weatherforecast.core.data.source.mapper.mapResponseCodeToThrowable
import com.awesomejim.weatherforecast.core.data.source.mapper.toCoreModel
import com.awesomejim.weatherforecast.core.data.source.mapper.toLocationItemDataList
import com.awesomejim.weatherforecast.core.data.utils.RetrialResult
import com.awesomejim.weatherforecast.core.network.ApiService
import com.awesomejim.weatherforecast.core.printTrace
import timber.log.Timber
import javax.inject.Inject

class DefaultRemoteWeatherDataSource @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {
    override suspend fun fetchWeatherDataWithCoordinates(
        defaultLocation: com.awesomejim.weatherforecast.core.DefaultLocation,
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
                throw mapResponseCodeToThrowable(response.code())
            }
        } catch (e: Exception) {
            Timber.e(
                "<<<<<<<<<fetchWeatherDataWithCoordinates Exception>>>>>>>>>>: %s",
                e.message
            )
            printTrace(e)
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
                val weatherData = response.body()!!.toCoreModel(addSummary = true)
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
            printTrace(e)
            throw e
        }

    override suspend fun fetchWeatherForecastWithCoordinates(
        defaultLocation: com.awesomejim.weatherforecast.core.DefaultLocation,
        units: String
    ): RetrialResult<List<LocationItemData>> =
        try {
            val response = apiService.fetchWeatherForecast(
                appid = BuildConfig.OPEN_WEATHER_APP_ID,
                units = units,
                latitude = defaultLocation.latitude,
                longitude = defaultLocation.longitude
            )
            if (response.isSuccessful && response.body() != null) {
                val weatherData = response.body()!!.toLocationItemDataList(units)
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
