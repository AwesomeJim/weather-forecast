
package com.awesomejim.weatherforecast.data.source.local.converter

import androidx.room.TypeConverter
import java.util.*

/**
 * Converts Date to a Long and Back
 * This enable room to save the date
 */
object DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}