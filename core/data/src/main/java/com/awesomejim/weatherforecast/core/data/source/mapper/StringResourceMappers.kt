package com.awesomejim.weatherforecast.core.data.source.mapper

import androidx.annotation.StringRes
import com.awesomejim.weatherforecast.core.data.R
import com.awesomejim.weatherforecast.core.data.utils.ErrorType

@StringRes
fun ErrorType.toResourceId(): Int = when (this) {
    ErrorType.SERVER -> R.string.error_server
    ErrorType.GENERIC -> R.string.error_generic
    ErrorType.IO_CONNECTION -> R.string.error_connection
    ErrorType.UNAUTHORIZED -> R.string.error_unauthorized
    ErrorType.CLIENT -> R.string.error_client
}

