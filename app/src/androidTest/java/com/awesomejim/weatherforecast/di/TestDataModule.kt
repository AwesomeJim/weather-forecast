package com.awesomejim.weatherforecast.di

import android.content.Context
import androidx.room.Room
import com.awesomejim.weatherforecast.data.source.local.db.LocationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestDataModule {
    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(
            context, LocationDatabase::class.java
        ).allowMainThreadQueries()
            .build()

}