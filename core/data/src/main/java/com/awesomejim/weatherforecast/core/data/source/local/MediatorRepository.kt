package com.awesomejim.weatherforecast.core.data.source.local

import com.awesomejim.weatherforecast.core.DefaultLocation
import com.awesomejim.weatherforecast.core.LocationItemData
import com.awesomejim.weatherforecast.core.network.RetrialResult

interface MediatorRepository {

    suspend fun fetchWeatherDataWithCoordinates(
        defaultLocation: DefaultLocation,
        units: String,
        locationId: Long?
    ): RetrialResult<LocationItemData>
}
