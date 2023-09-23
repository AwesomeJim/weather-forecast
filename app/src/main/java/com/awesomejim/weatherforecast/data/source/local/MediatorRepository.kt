package com.awesomejim.weatherforecast.data.source.local

import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.di.network.RetrialResult
import kotlinx.coroutines.flow.Flow


interface MediatorRepository {

    suspend fun fetchWeatherDataWithCoordinates(
        defaultLocation: DefaultLocation,
        units: String,
        locationId: Long?
    ) : Flow<RetrialResult<LocationItemData>>

}
