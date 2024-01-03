package com.awesomejim.weatherforecast.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.awesomejim.weatherforecast.core.database.converter.DateConverter
import com.awesomejim.weatherforecast.core.database.dao.LocationItemDao
import com.awesomejim.weatherforecast.core.database.entity.LocationItemEntity


@Database(entities = [LocationItemEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationItemDao
}
