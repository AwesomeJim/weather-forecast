package com.awesomejim.weatherforecast.di.data

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
    ): com.awesomejim.weatherforecast.core.database.LocationDatabase {
        return Room.databaseBuilder(
            appContext,
            com.awesomejim.weatherforecast.core.database.LocationDatabase::class.java,
            "weather_forecast_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocationDao(appDatabase: com.awesomejim.weatherforecast.core.database.LocationDatabase): com.awesomejim.weatherforecast.core.database.dao.LocationItemDao {
        return appDatabase.locationDao()
    }
}
