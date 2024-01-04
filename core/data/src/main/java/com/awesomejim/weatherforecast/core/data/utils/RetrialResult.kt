package com.awesomejim.weatherforecast.core.data.utils

sealed class RetrialResult<T> {
    data class Success<T>(val data: T) : RetrialResult<T>()

    data class Error<T>(val errorType: ErrorType) : RetrialResult<T>()
}

enum class ErrorType {
    CLIENT,
    SERVER,
    GENERIC,
    IO_CONNECTION,
    UNAUTHORIZED
}
