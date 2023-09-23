package com.awesomejim.weatherforecast.data.source.local

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class MediatorWeatherRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val networkHelper: NetworkHelper,
    private val locationItemDao: LocationItemDao
) : MediatorRepository {
    /**
     * Fetch weather data with coordinates
     *
     * @param defaultLocation location coordinates
     * @param units - preferred units
     * @param locationId Location Id Retried from a previous success call
     * @return
     */
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
                        Timber.e("DefaultWeatherRepository weatherData :: ${weatherData.locationName}")
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
            // there is no internet access let's check and show last saved location if it exits otherwise show No internet error
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
}