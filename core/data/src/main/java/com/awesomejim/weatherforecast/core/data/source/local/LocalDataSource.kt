package com.awesomejim.weatherforecast.core.data.source.local

import com.awesomejim.weatherforecast.core.LocationItemData
import com.awesomejim.weatherforecast.core.data.source.mapper.toLocationEntity
import com.awesomejim.weatherforecast.core.data.source.mapper.toLocationItem
import com.awesomejim.weatherforecast.core.database.dao.LocationItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class LocalDataSource @Inject constructor(private val locationItemDao: LocationItemDao) {

    suspend fun getLocationDataById(id: Long): LocationItemData? = withContext(Dispatchers.IO) {
        return@withContext locationItemDao.getLocationById(id)?.toLocationItem()
    }

    fun loadAllLocation(): Flow<List<LocationItemData>?> {
        return locationItemDao.loadAllLocation()
            .map {
                it?.map { item ->
                    item.toLocationItem()
                }
            }
    }

    suspend fun updateLocation(locationEntry: LocationItemData) = withContext(Dispatchers.IO) {
        locationItemDao.updateLocation(locationEntry.toLocationEntity())
    }

    suspend fun insertLocation(entry: LocationItemData) = withContext(Dispatchers.IO) {
        locationItemDao.insertLocation(entry.toLocationEntity())
    }

    suspend fun deleteLocation(entry: LocationItemData) = withContext(Dispatchers.IO) {
        locationItemDao.deleteLocation(entry.toLocationEntity())
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        locationItemDao.clearAll()
    }
}
