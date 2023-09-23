package com.awesomejim.weatherforecast.data.source.local

import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.di.network.RetrialResult
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getLocationData(id: Int): Flow<RetrialResult<LocationItemData>>
}