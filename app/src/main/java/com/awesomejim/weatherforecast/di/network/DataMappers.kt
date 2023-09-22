package com.awesomejim.weatherforecast.di.network

import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.WeatherForecastApp
import com.awesomejim.weatherforecast.data.model.Coordinates
import com.awesomejim.weatherforecast.data.model.WeatherItemDataModel
import com.awesomejim.weatherforecast.data.model.WeatherStatus
import com.awesomejim.weatherforecast.data.model.Wind
import com.awesomejim.weatherforecast.utilities.ClientException
import com.awesomejim.weatherforecast.utilities.GenericException
import com.awesomejim.weatherforecast.utilities.ServerException
import com.awesomejim.weatherforecast.utilities.UnauthorizedException
import com.awesomejim.weatherforecast.utilities.Units
import java.io.IOException
import java.net.HttpURLConnection
import java.util.Calendar
import java.util.TimeZone
import kotlin.math.roundToInt


fun WeatherItemResponse.toCoreModel(unit: String): WeatherItemDataModel =
    WeatherItemDataModel(
        locationName = "$locationName - ${countryDetails.locationCountry}",
        locationId = locationId,
        locationNameTimeZoneShift = locationNameTimeZoneShift,
        locationDate = forecastedTime,
        locationCoordinates = locationCoordinates.toCoreModel(),
        locationWeather = WeatherStatus(
            weatherConditionId = weatherConditionResponse[0].id, // we pic
            weatherCondition = weatherConditionResponse[0].main,
            weatherConditionDescription =  weatherConditionResponse[0].description,
            weatherTemp = formatTemperatureValue(currentWeatherMain.weatherTemp,unit),
            weatherTempMin = formatTemperatureValue(currentWeatherMain.weatherTempMin,unit),
            weatherTempMax = formatTemperatureValue(currentWeatherMain.weatherTempMax,unit),
            weatherPressure = currentWeatherMain.weatherPressure,
            weatherHumidity = currentWeatherMain.weatherHumidity,
            weatherWind = currentWeatherWind.toCoreModel()
        ),
        locationWeatherDay = getWeatherDay(forecastedTime)
    )


fun CoordinatesResponse.toCoreModel(): Coordinates =
    Coordinates(
        longitude = longitude,
        latitude = latitude)

fun WindResponse.toCoreModel(): Wind =
    Wind(speed = speed, deg = degree)


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

private fun getWeatherDay(utcInMillis: Long): Int {
    val calendar = Calendar.getInstance(TimeZone.getDefault())
    calendar.timeInMillis = utcInMillis * 1000
    return calendar[Calendar.DAY_OF_MONTH]
}

private fun formatTemperatureValue(temperature: Double, unit: String): String =
    "${temperature.roundToInt()}${getUnitSymbols(unit = unit)}"

private fun getUnitSymbols(unit: String) = when (unit) {
    Units.METRIC.value -> Units.METRIC.tempLabel
    Units.IMPERIAL.value -> Units.IMPERIAL.tempLabel
    Units.STANDARD.value -> Units.STANDARD.tempLabel
    else -> "N/A"
}

/**
 * This method uses the wind direction in degrees to determine compass direction as a
 * String. (eg NW) The method will return the wind String in the following form: "2 km/h SW"
 *
 * @param context   Android Context to access preferences and resources
 * @param windSpeed Wind speed in kilometers / hour
 * @param degrees   Degrees as measured on a compass, NOT temperature degrees!
 * See https://www.mathsisfun.com/geometry/degrees.html
 *
 * @return Wind String in the following form: "2 km/h SW"
 */
fun getFormattedWind(windSpeed: Double, degrees: Double): String {
    val windFormat = R.string.format_wind_kmh

    var direction = "Unknown"
    when {
        degrees >= 337.5 || degrees < 22.5 -> {
            direction = "N"
        }
        degrees >= 22.5 && degrees < 67.5 -> {
            direction = "NE"
        }
        degrees >= 67.5 && degrees < 112.5 -> {
            direction = "E"
        }
        degrees >= 112.5 && degrees < 157.5 -> {
            direction = "SE"
        }
        degrees >= 157.5 && degrees < 202.5 -> {
            direction = "S"
        }
        degrees >= 202.5 && degrees < 247.5 -> {
            direction = "SW"
        }
        degrees >= 247.5 && degrees < 292.5 -> {
            direction = "W"
        }
        degrees >= 292.5 && degrees < 337.5 -> {
            direction = "NW"
        }
    }
    return String.format(WeatherForecastApp.applicationContext().getString(windFormat), windSpeed, direction)
}