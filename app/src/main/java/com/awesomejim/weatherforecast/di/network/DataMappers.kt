package com.awesomejim.weatherforecast.di.network

import com.awesomejim.weatherforecast.utilities.ClientException
import com.awesomejim.weatherforecast.utilities.GenericException
import com.awesomejim.weatherforecast.utilities.ServerException
import com.awesomejim.weatherforecast.utilities.UnauthorizedException
import java.io.IOException
import java.net.HttpURLConnection






fun mapResponseCodeToThrowable(code: Int): Throwable = when (code) {
    HttpURLConnection.HTTP_UNAUTHORIZED -> UnauthorizedException("Unauthorized access : $code")
    in 400..499 -> ClientException("Client error : $code")
    in 500..600 -> ServerException("Server error : $code")
    else -> GenericException("Generic error : $code")
}

fun mapThrowableToErrorType(throwable: Throwable): ErrorType {
    val errorType = when (throwable) {
        is IOException -> ErrorType.IO_CONNECTION
        is ClientException -> ErrorType.CLIENT
        is ServerException -> ErrorType.SERVER
        is UnauthorizedException -> ErrorType.UNAUTHORIZED
        else -> ErrorType.GENERIC
    }
    return errorType
}