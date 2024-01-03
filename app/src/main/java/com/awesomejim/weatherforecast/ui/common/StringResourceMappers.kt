package com.awesomejim.weatherforecast.ui.common

import androidx.annotation.StringRes
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.core.network.ErrorType

@StringRes
fun com.awesomejim.weatherforecast.core.network.ErrorType.toResourceId(): Int = when (this) {
    com.awesomejim.weatherforecast.core.network.ErrorType.SERVER -> R.string.error_server
    com.awesomejim.weatherforecast.core.network.ErrorType.GENERIC -> R.string.error_generic
    com.awesomejim.weatherforecast.core.network.ErrorType.IO_CONNECTION -> R.string.error_connection
    com.awesomejim.weatherforecast.core.network.ErrorType.UNAUTHORIZED -> R.string.error_unauthorized
    com.awesomejim.weatherforecast.core.network.ErrorType.CLIENT -> R.string.error_client
}

