package com.awesomejim.weatherforecast.core.data.source.local

import com.awesomejim.weatherforecast.core.DefaultLocation
import com.awesomejim.weatherforecast.core.LocationItemData
import com.awesomejim.weatherforecast.core.data.source.mapper.mapThrowableToErrorType
import com.awesomejim.weatherforecast.core.data.source.mapper.toLocationEntity
import com.awesomejim.weatherforecast.core.data.source.mapper.toLocationItem
import com.awesomejim.weatherforecast.core.data.source.remote.RemoteDataSource
import com.awesomejim.weatherforecast.core.database.dao.LocationItemDao
import com.awesomejim.weatherforecast.core.network.ErrorType
import com.awesomejim.weatherforecast.core.network.NetworkHelper
import com.awesomejim.weatherforecast.core.network.RetrialResult

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

                        // when pulling data from open weather sometimes we
                        // get different location id with the same coordinates
                        locationId?.let {
                            weatherData.locationId = it
                        }
                        locationItemDao.insertLocation(weatherData.toLocationEntity())
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
            // there is no internet access let's check
            // and show last saved location if it exits otherwise show No internet error
            if (locationId != null) {
                val savedData = locationItemDao.getLocationById(locationId)
                if (savedData != null) {
                    RetrialResult.Success(savedData.toLocationItem())
                } else {
                    RetrialResult.Error(ErrorType.IO_CONNECTION)
                }
            } else {
                RetrialResult.Error(ErrorType.IO_CONNECTION)
            }
        }
    }
}
