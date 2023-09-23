package com.awesomejim.weatherforecast.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.awesomejim.weatherforecast.data.source.local.converter.DateConverter
import com.awesomejim.weatherforecast.data.source.local.dao.LocationItemDao
import com.awesomejim.weatherforecast.data.source.local.entity.LocationItemEntity

@Database(entities = [LocationItemEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class LocationDatabase :RoomDatabase(){
    abstract fun locationDao(): LocationItemDao
}