package com.awesomejim.weatherforecast.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("location_item")
data class LocationItemEntity(
    @ColumnInfo(name = "location_id") @PrimaryKey val locationId: Long,
    @ColumnInfo(name = "location_name") val locationName: String,
    @ColumnInfo(name = "location_time_zone_shift") val locationTimeZoneShift: Long,
    @ColumnInfo(name = "location_data_time") var locationDataTime: Long,
    @ColumnInfo(name = "location_longitude") val locationLongitude: Double,
    @ColumnInfo(name = "location_latitude") val locationLatitude: Double,
    @ColumnInfo(name = "location_weather_day") val locationWeatherDay: Int,
    @ColumnInfo(name = "location_data_last_update") val locationDataLastUpdate: Date,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
    @Embedded val locationWeatherInfo: WeatherStatusInfoEntity
)

data class WeatherStatusInfoEntity(
    @ColumnInfo(name = "condition_id") val weatherConditionId: Int,
    @ColumnInfo(name = "condition") val weatherCondition: String,
    @ColumnInfo(name = "condition _description") val weatherConditionDescription: String,
    @ColumnInfo(name = "weather_condition_icon") val weatherConditionIcon: String,
    @ColumnInfo(name = "temp") val weatherTemp: Float,
    @ColumnInfo(name = "temp_min") val weatherTempMin: Float,
    @ColumnInfo(name = "temp_max") val weatherTempMax: Float,
    @ColumnInfo(name = "feels_like") val weatherTempFeelsLike: Float,
    @ColumnInfo(name = "pressure") val weatherPressure: Double,
    @ColumnInfo(name = "humidity") val weatherHumidity: Double,
    @ColumnInfo(name = "wind_speed") val weatherWindSpeed: Double,
    @ColumnInfo(name = "wind_direction") val weatherWindDegrees: Double,
    @ColumnInfo(name = "visibility") val weatherVisibility: Double
)
