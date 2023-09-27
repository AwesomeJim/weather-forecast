package com.awesomejim.weatherforecast.data

import com.awesomejim.weatherforecast.data.model.DefaultLocation
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun setUnits(units: String)

    suspend fun getUnits(): Flow<String>
    fun getAvailableUnits(): List<String>

    suspend fun setDefaultLocation(defaultLocation: DefaultLocation)

    suspend fun getDefaultLocation(): Flow<DefaultLocation>

    fun getAppVersion(): String
}
