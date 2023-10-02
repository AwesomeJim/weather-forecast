package com.awesomejim.weatherforecast.di.network

/**
 * These utilities will be used to communicate with the weather servers.
 */
object NetworkUtils {
    val TAG = NetworkUtils::class.java.simpleName

    /* The format we want our API to return */
    const val format = "json"

    /* The units we want our API to return */
    const val units = "metric"

    /* The query parameter allows us to provide a location string to the API */
    const val QUERY_PARAM = "q"
    const val LAT_PARAM = "lat"
    const val LON_PARAM = "lon"

    /* The format parameter allows us to designate whether we want JSON or XML from our API */
    const val FORMAT_PARAM = "mode"

    /* The units parameter allows us to designate whether we want metric units or imperial units */
    const val UNITS_PARAM = "units"

    /* The days parameter allows us to designate how many days of weather data we want */
    const val DAYS_PARAM = "cnt"

    /* The query parameter allows us to provide a App id*/
    const val APP_ID_PARAM = "appid"
}
