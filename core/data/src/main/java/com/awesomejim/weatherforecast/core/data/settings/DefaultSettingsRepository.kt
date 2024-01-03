package com.awesomejim.weatherforecast.core.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import com.awesomejim.weatherforecast.core.DefaultLocation
import com.awesomejim.weatherforecast.core.Units
import com.awesomejim.weatherforecast.core.data.BuildConfig
import com.awesomejim.weatherforecast.core.data.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class DefaultSettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private val PREF_UNITS by lazy { stringPreferencesKey("units") }
    private val PREF_LAT_LNG by lazy { stringPreferencesKey("lat_lng") }

    // Nairobi
    private val DEFAULT_LONGITUDE = 36.820248
    private val DEFAULT_LATITUDE = -1.29729

    override suspend fun setUnits(units: String) {
        set(key = PREF_UNITS, value = units)
    }

    override suspend fun getUnits(): Flow<String> =
        get(key = PREF_UNITS, default = Units.METRIC.value)

    override fun getAvailableUnits(): List<String> = Units.values().map { it.value }

    override suspend fun setDefaultLocation(defaultLocation: DefaultLocation) {
        set(key = PREF_LAT_LNG, value = "${defaultLocation.latitude}/${defaultLocation.longitude}")
    }

    override suspend fun getDefaultLocation(): Flow<DefaultLocation> {
        return get(
            key = PREF_LAT_LNG,
            default = "$DEFAULT_LATITUDE/$DEFAULT_LONGITUDE"
        ).map { latlng ->
            val latLngList = latlng.split("/").map { it.toDouble() }
            DefaultLocation(
                latitude = latLngList[0],
                longitude = latLngList[1]
            )
        }
    }

    private suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { settings ->
            settings[key] = value
        }
    }

    private fun <T> get(key: Preferences.Key<T>, default: T): Flow<T> {
        return context.dataStore.data.map { settings ->
            settings[key] ?: default
        }
    }

    override fun getAppVersion(): String =
        "App Version : ${BuildConfig.BUILD_TYPE}-${BuildConfig.BUILD_TYPE}"
}
