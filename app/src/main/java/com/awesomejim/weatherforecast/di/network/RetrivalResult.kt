package com.awesomejim.weatherforecast.di.network

sealed class RetrivalResult<T> {
    data class Success<T>(val data: T) : RetrivalResult<T>()

    data class Error<T>(val errorType: ErrorType) : RetrivalResult<T>()
}

enum class ErrorType {
    CLIENT,
    SERVER,
    GENERIC,
    IO_CONNECTION,
    UNAUTHORIZED
}
