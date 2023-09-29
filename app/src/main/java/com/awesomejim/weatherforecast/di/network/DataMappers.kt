package com.awesomejim.weatherforecast.di.network

import com.awesomejim.weatherforecast.data.model.ForecastMoreDetails
import com.awesomejim.weatherforecast.data.model.HourlyWeatherData
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.data.model.WeatherStatusInfo
import com.awesomejim.weatherforecast.ui.common.getDate
import com.awesomejim.weatherforecast.utilities.ClientException
import com.awesomejim.weatherforecast.utilities.GenericException
import com.awesomejim.weatherforecast.utilities.ServerException
import com.awesomejim.weatherforecast.utilities.UnauthorizedException
import com.awesomejim.weatherforecast.utilities.Units
import com.awesomejim.weatherforecast.utilities.WeatherUtils
import java.io.IOException
import java.net.HttpURLConnection
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

fun WeatherItemResponse.toCoreModel(addSummary: Boolean = false): LocationItemData {
    val locationItemData = LocationItemData(
        locationName = "$locationName - ${countryDetails.locationCountry}",
        locationId = locationId,
        locationTimeZoneShift = locationNameTimeZoneShift,
        locationDataTime = forecastedTime,
        locationLongitude = locationCoordinates.longitude,
        locationLatitude = locationCoordinates.latitude,
        locationWeatherInfo = WeatherStatusInfo(
            weatherConditionId = weatherConditionResponse[0].id,
            weatherCondition = weatherConditionResponse[0].main,
            weatherConditionDescription = weatherConditionResponse[0].description.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            },
            weatherConditionIcon = weatherConditionResponse[0].icon,
            weatherTemp = currentWeatherMain.weatherTemp,
            weatherTempMin = currentWeatherMain.weatherTempMin,
            weatherTempMax = currentWeatherMain.weatherTempMax,
            weatherTempFeelsLike = currentWeatherMain.weatherTempFeelsLike,
            weatherPressure = currentWeatherMain.weatherPressure,
            weatherHumidity = currentWeatherMain.weatherHumidity,
            weatherWindSpeed = currentWeatherWind.speed,
            weatherWindDegrees = currentWeatherWind.degree,
            weatherVisibility = currentWeatherVisibility,
        ),
        locationWeatherDay = getWeatherDay(forecastedTime),
        locationDataLastUpdate = Date()
    )
    if (addSummary) {

        val windDirection =
            getFormattedWind(locationItemData.locationWeatherInfo.weatherWindDegrees)

        val visibility = locationItemData.locationWeatherInfo.weatherVisibility / 1000

        val forecastMoreDetails = ForecastMoreDetails(
            windDetails = "%1\$1.0f km/h %2\$s".format(
                locationItemData.locationWeatherInfo.weatherWindSpeed.toFloat(),
                windDirection
            ),
            humidityDetails = "%1.0f %%"
                .format(locationItemData.locationWeatherInfo.weatherHumidity.toFloat()),
            visibilityDetails = "$visibility km",
            pressureDetails = "%1.0f hPa"
                .format(locationItemData.locationWeatherInfo.weatherPressure.toFloat()),
            hourlyWeatherData = listOf()
        )

        locationItemData.forecastMoreDetails = forecastMoreDetails
    }
    return locationItemData
}

/*
data class ForecastMoreDetails(
    val windDetails: String,
    val humidityDetails: String,
    val visibilityDetails: String,
    val pressureDetails: String,
    val hourlyWeatherData: List<HourlyWeatherData>
)

data class HourlyWeatherData(
    val temperature: String,
    @DrawableRes val drawableIcon: Int,
    val hourTime: String
)

 */
fun ForecastResponse.toLocationItemDataList(units: String): List<LocationItemData> {
    val responseList = mutableListOf<LocationItemData>()
    foreCastList.forEach { list ->
        with(list) {
            val response = LocationItemData(
                locationName = "${foreCastCity.locationName} - ${foreCastCity.locationCountry}",
                locationId = foreCastCity.locationId,
                locationTimeZoneShift = foreCastCity.locationNameTimeZoneShift,
                locationDataTime = forecastedTime,
                locationLongitude = foreCastCity.locationCoordinates.longitude,
                locationLatitude = foreCastCity.locationCoordinates.latitude,
                locationWeatherInfo = WeatherStatusInfo(
                    weatherConditionId = weatherConditionResponse[0].id,
                    weatherCondition = weatherConditionResponse[0].main,
                    weatherConditionDescription =
                    weatherConditionResponse[0].description
                        .replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                    weatherConditionIcon = weatherConditionResponse[0].icon,
                    weatherTemp = currentWeatherMain.weatherTemp,
                    weatherTempMin = currentWeatherMain.weatherTempMin,
                    weatherTempMax = currentWeatherMain.weatherTempMax,
                    weatherTempFeelsLike = currentWeatherMain.weatherTempFeelsLike,
                    weatherPressure = currentWeatherMain.weatherPressure,
                    weatherHumidity = currentWeatherMain.weatherHumidity,
                    weatherWindSpeed = currentWeatherWind.speed,
                    weatherWindDegrees = currentWeatherWind.degree,
                    weatherVisibility = currentWeatherVisibility,
                ),
                locationWeatherDay = getWeatherDay(forecastedTime),
                locationDataLastUpdate = Date()
            )
            responseList.add(response)
        }
    }
    /**
     * open weather forecast returns response for 5 days with data every 3 hours (5*8)
     * I had to have way to group forecast of the  day by using the the calender instance day of the month then grouping the list
     * with a distinct day of the, thus we should have 5 groups with 8 entries for a single day we will have only one
     */
    val todayDate = getTodayDay()
    val groupedMap = responseList.groupBy { it.locationWeatherDay }.toSortedMap()
    val result = mutableListOf<LocationItemData>()
    groupedMap.forEach { (num, list) ->
        if (num != todayDate) { // we skip the first item which equals today
            val hourlyWeatherDataList = mutableListOf<HourlyWeatherData>()
            for (item in list) {
                val temperature =
                    formatTemperatureValue(item.locationWeatherInfo.weatherTemp, units)
                val drawableIcon = WeatherUtils
                    .iconIdForWeatherCondition(item.locationWeatherInfo.weatherConditionId)
                val hourTime = getDate(item.locationDataTime, "HH:SS")
                val hourlyData = HourlyWeatherData(
                    temperature = temperature,
                    drawableIcon = drawableIcon,
                    hourTime = hourTime
                )
                hourlyWeatherDataList.add(hourlyData)
            }
            val fList = list[0]
            val windDirection = getFormattedWind(fList.locationWeatherInfo.weatherWindDegrees)

            val visibility = fList.locationWeatherInfo.weatherVisibility / 1000
            val forecastMoreDetails = ForecastMoreDetails(
                windDetails = "%1\$1.0f km/h %2\$s"
                    .format(
                        fList.locationWeatherInfo.weatherWindSpeed.toFloat(),
                        windDirection
                    ),
                humidityDetails = "%1.0f %%"
                    .format(fList.locationWeatherInfo.weatherHumidity.toFloat()),
                visibilityDetails = "$visibility km",
                pressureDetails = "%1.0f hPa"
                    .format(fList.locationWeatherInfo.weatherPressure.toFloat()),
                hourlyWeatherData = hourlyWeatherDataList
            )

            fList.forecastMoreDetails = forecastMoreDetails
            result.add(fList)
        }
    }
    // sort by date
    return result.sortedBy { it.locationDataTime }
}

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

private fun getTodayDay(): Int {
    val calendar = Calendar.getInstance(TimeZone.getDefault())
    return calendar[Calendar.DAY_OF_MONTH]
}

fun formatTemperatureValue(temperature: Float, unit: String): String =
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
 * @param degrees   Degrees as measured on a compass, NOT temperature degrees!
 * See https://www.mathsisfun.com/geometry/degrees.html
 *
 * @return Wind String in the following form: "2 km/h SW"
 */
fun getFormattedWind(degrees: Double): String {
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

        degrees >= 292.5 -> {
            direction = "NW"
        }
    }
    return direction
}
