package com.awesomejim.weatherforecast.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.awesomejim.weatherforecast.data.source.local.entity.LocationItemEntity


@Dao
interface LocationItemDao {
    @Query("SELECT * FROM location_item ORDER BY location_data_last_update DESC")
    fun loadAllLocation(): List<LocationItemEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(locationEntry: LocationItemEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLocation(locationEntry: LocationItemEntity)

    @Delete
    suspend fun deleteLocation(locationEntry: LocationItemEntity)

    @Query("SELECT * FROM location_item WHERE location_id=:id")
    suspend fun getLocationById(id: Int): LocationItemEntity?


    @Query("DELETE FROM location_item")
    suspend fun clearAll()
}