package com.awesomejim.weatherforecast.core.database.di

import android.content.Context
import androidx.room.Room
import com.awesomejim.weatherforecast.core.database.LocationDatabase
import com.awesomejim.weatherforecast.core.database.dao.LocationItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): LocationDatabase {
        return Room.databaseBuilder(
            appContext,
            LocationDatabase::class.java,
            "weather_forecast_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocationDao(appDatabase: LocationDatabase): LocationItemDao {
        return appDatabase.locationDao()
    }
}
