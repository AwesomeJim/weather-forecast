package com.awesomejim.weatherforecast.di.data

import com.awesomejim.weatherforecast.data.DefaultWeatherRepository
import com.awesomejim.weatherforecast.data.SettingsRepository
import com.awesomejim.weatherforecast.data.WeatherRepository
import com.awesomejim.weatherforecast.data.settings.DefaultSettingsRepository
import com.awesomejim.weatherforecast.data.source.local.MediatorRepository
import com.awesomejim.weatherforecast.data.source.local.MediatorWeatherRepository
import com.awesomejim.weatherforecast.data.source.remote.DefaultRemoteWeatherDataSource
import com.awesomejim.weatherforecast.data.source.remote.RemoteDataSource
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
