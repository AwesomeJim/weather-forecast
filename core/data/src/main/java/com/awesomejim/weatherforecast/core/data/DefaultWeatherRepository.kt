package com.awesomejim.weatherforecast.core.data

import com.awesomejim.weatherforecast.core.LocationItemData
import com.awesomejim.weatherforecast.core.data.source.mapper.mapThrowableToErrorType
import com.awesomejim.weatherforecast.core.data.source.remote.RemoteDataSource
import com.awesomejim.weatherforecast.core.data.utils.ErrorType
import com.awesomejim.weatherforecast.core.data.utils.RetrialResult
import com.awesomejim.weatherforecast.core.network.NetworkHelper
import timber.log.Timber
import javax.inject.Inject

class DefaultWeatherRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val networkHelper: NetworkHelper
) : WeatherRepository {
    /**
     * Fetch weather data with coordinates
     *
     * @param defaultLocation location coordinates
     * @param units - prefered units
     * @param locationId Location Id Retrived from a previous success call
     * @return
     */
    override suspend fun fetchWeatherDataWithCoordinates(
        defaultLocation: com.awesomejim.weatherforecast.core.DefaultLocation,
        units: String
    ): RetrialResult<LocationItemData> {
        return if (networkHelper.isNetworkConnected()) {
            try {
                val remoteData = remoteDataSource.fetchWeatherDataWithCoordinates(
                    defaultLocation = defaultLocation,
                    units = units
                )
                when (remoteData) {
                    is RetrialResult.Success -> {
                        val weatherData = remoteData.data
                        Timber.e(
                            "DefaultWeather" +
                                "Repository weatherData :: ${weatherData.locationName}"
                        )
                        RetrialResult.Success(weatherData)
                    }

                    is RetrialResult.Error -> {
                        RetrialResult.Error(remoteData.errorType)
                    }
                }
            } catch (throwable: Throwable) {
                Timber.e("DefaultWeatherRepository throwable :: ${throwable.message}")
                val errorType = mapThrowableToErrorType(throwable)
                RetrialResult.Error(errorType)
            }
        } else {
            RetrialResult.Error(ErrorType.IO_CONNECTION)
        }
    }

    override suspend fun fetchWeatherDataWithLocationQuery(
        locationQuery: String,
        units: String
    ): RetrialResult<LocationItemData> {
        return if (networkHelper.isNetworkConnected()) {
            try {
                val remoteData = remoteDataSource.fetchWeatherDataWithLocationQuery(
                    locationQuery = locationQuery,
                    units = units
                )
                when (remoteData) {
                    is RetrialResult.Success -> {
                        val weatherData = remoteData.data
                        Timber.e(
                            "DefaultWeather" +
                                "Repository weatherData :: ${weatherData.locationName}"
                        )
                        RetrialResult.Success(weatherData)
                    }

                    is RetrialResult.Error -> {
                        RetrialResult.Error(remoteData.errorType)
                    }
                }
            } catch (throwable: Throwable) {
                val errorType = mapThrowableToErrorType(throwable)
                RetrialResult.Error(errorType)
            }
        } else {
            RetrialResult.Error(ErrorType.IO_CONNECTION)
        }
    }

    override suspend fun fetchWeatherForecastWithCoordinates(
        defaultLocation: com.awesomejim.weatherforecast.core.DefaultLocation,
        units: String
    ): RetrialResult<List<LocationItemData>> {
        return if (networkHelper.isNetworkConnected()) {
            try {
                val remoteData = remoteDataSource.fetchWeatherForecastWithCoordinates(
                    defaultLocation = defaultLocation,
                    units = units
                )
                when (remoteData) {
                    is RetrialResult.Success -> {
                        val weatherData = remoteData.data
                        Timber.e(
                            "DefaultWeather" +
                                "Repository weatherData :: ${weatherData.size}"
                        )
                        RetrialResult.Success(weatherData)
                    }

                    is RetrialResult.Error -> {
                        RetrialResult.Error(remoteData.errorType)
                    }
                }
            } catch (throwable: Throwable) {
                val errorType = mapThrowableToErrorType(throwable)
                RetrialResult.Error(errorType)
            }
        } else {
            RetrialResult.Error(ErrorType.IO_CONNECTION)
        }
    }
}
