package com.awesomejim.weatherforecast.core.data.di

import com.awesomejim.weatherforecast.core.data.DefaultWeatherRepository
import com.awesomejim.weatherforecast.core.data.SettingsRepository
import com.awesomejim.weatherforecast.core.data.WeatherRepository
import com.awesomejim.weatherforecast.core.data.settings.DefaultSettingsRepository
import com.awesomejim.weatherforecast.core.data.source.local.MediatorRepository
import com.awesomejim.weatherforecast.core.data.source.local.MediatorWeatherRepository
import com.awesomejim.weatherforecast.core.data.source.remote.DefaultRemoteWeatherDataSource
import com.awesomejim.weatherforecast.core.data.source.remote.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface DataRepositoryModule {

    @Binds
    fun bindWeatherRepository(weatherRepository: DefaultWeatherRepository):
            WeatherRepository

    @Binds
    fun bindRemoteWeatherDataSource(remoteWeatherDataSource: DefaultRemoteWeatherDataSource):
            RemoteDataSource

    @Binds
    fun bindSettingsRepository(settingsRepository: DefaultSettingsRepository):
            SettingsRepository

    @Binds
    fun bindMediatorRepository(mediatorRepository: MediatorWeatherRepository):
            MediatorRepository
}
