package com.awesomejim.weatherforecast.data

import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.data.source.remote.RemoteDataSource
import com.awesomejim.weatherforecast.di.network.ErrorType
import com.awesomejim.weatherforecast.di.network.NetworkHelper
import com.awesomejim.weatherforecast.di.network.RetrialResult
import com.awesomejim.weatherforecast.di.network.mapThrowableToErrorType
import javax.inject.Inject

class DefaultWeatherRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val networkHelper: NetworkHelper
) : WeatherRepository {
    override suspend fun fetchWeatherDataWithCoordinates(
        defaultLocation: DefaultLocation,
        units: String
    ): RetrialResult<LocationItemData> =
        if (networkHelper.isNetworkConnected()) {
            try {
                remoteDataSource.fetchWeatherDataWithCoordinates(
                    defaultLocation = defaultLocation,
                    units = units
                )
            }catch (throwable: Throwable) {
                val errorType = mapThrowableToErrorType(throwable)
                RetrialResult.Error(errorType)
            }
        } else {
            RetrialResult.Error(ErrorType.IO_CONNECTION)
        }

    override suspend fun fetchWeatherDataWithLocationQuery(
        locationQuery: String,
        units: String
    ): RetrialResult<LocationItemData> =
    if (networkHelper.isNetworkConnected()) {
        try {
            remoteDataSource.fetchWeatherDataWithLocationQuery(
                locationQuery = locationQuery,
                units = units
            )
        }catch (throwable: Throwable) {
            val errorType = mapThrowableToErrorType(throwable)
            RetrialResult.Error(errorType)
        }
    } else {
        RetrialResult.Error(ErrorType.IO_CONNECTION)
    }

}