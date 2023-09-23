package com.awesomejim.weatherforecast.data

import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.data.source.local.dao.LocationItemDao
import com.awesomejim.weatherforecast.data.source.mapper.toLocationEntity
import com.awesomejim.weatherforecast.data.source.mapper.toLocationItem
import com.awesomejim.weatherforecast.data.source.remote.RemoteDataSource
import com.awesomejim.weatherforecast.di.network.ErrorType
import com.awesomejim.weatherforecast.di.network.NetworkHelper
import com.awesomejim.weatherforecast.di.network.RetrialResult
import com.awesomejim.weatherforecast.di.network.mapThrowableToErrorType
import com.awesomejim.weatherforecast.ui.common.toResourceId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class DefaultWeatherRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val networkHelper: NetworkHelper,
    private val locationItemDao: LocationItemDao
) : WeatherRepository {
    override suspend fun fetchWeatherDataWithCoordinates(
        defaultLocation: DefaultLocation,
        units: String,
        locationId: Long?
    ): Flow<RetrialResult<LocationItemData>> = flow {
        if (networkHelper.isNetworkConnected()) {
            try {
                val remoteData = remoteDataSource.fetchWeatherDataWithCoordinates(
                    defaultLocation = defaultLocation,
                    units = units
                )
                when (remoteData) {
                    is RetrialResult.Success -> {
                        val weatherData = remoteData.data
                        Timber.e("weatherData :: ${weatherData.locationName}")
                        locationItemDao.insertLocation(weatherData.toLocationEntity())
                        emit(RetrialResult.Success(weatherData))
                    }
                    is RetrialResult.Error -> {
                        emit(RetrialResult.Error(remoteData.errorType))
                    }
                }
            } catch (throwable: Throwable) {
                val errorType = mapThrowableToErrorType(throwable)
                emit(RetrialResult.Error(errorType))
            }
        } else {
            if (locationId != null) {
                val savedData = locationItemDao.getLocationById(locationId)
                if (savedData != null) {
                    emit(RetrialResult.Success(savedData.toLocationItem()))
                } else {
                    emit(RetrialResult.Error(ErrorType.IO_CONNECTION))
                }
            } else {
                emit(RetrialResult.Error(ErrorType.IO_CONNECTION))
            }
        }
    }

    override suspend fun fetchWeatherDataWithLocationQuery(
        locationQuery: String,
        units: String
    ): Flow<RetrialResult<LocationItemData>> = flow {
        if (networkHelper.isNetworkConnected()) {
            try {
                remoteDataSource.fetchWeatherDataWithLocationQuery(
                    locationQuery = locationQuery,
                    units = units
                )
            } catch (throwable: Throwable) {
                val errorType = mapThrowableToErrorType(throwable)
                emit(RetrialResult.Error(errorType))
            }
        } else {
            emit(RetrialResult.Error(ErrorType.IO_CONNECTION))
        }
    }


}