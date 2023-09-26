package com.awesomejim.weatherforecast.ui.common

import androidx.annotation.StringRes
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.di.network.ErrorType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@StringRes
fun ErrorType.toResourceId(): Int = when (this) {
    ErrorType.SERVER -> R.string.error_server
    ErrorType.GENERIC -> R.string.error_generic
    ErrorType.IO_CONNECTION -> R.string.error_connection
    ErrorType.UNAUTHORIZED -> R.string.error_unauthorized
    ErrorType.CLIENT -> R.string.error_client
}


fun getDate(utcInMillis: Long, formatPattern: String): String {
    val sdf = SimpleDateFormat(formatPattern, Locale.getDefault())
    val dateFormat = Date(utcInMillis * 1000)
    return sdf.format(dateFormat)
}


fun getUpdatedOnDate(date: Date): String {
    val DATE_FORMAT = "EEE, d MMM yyyy HH:mm aa"
    val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(date)
}