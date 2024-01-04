package com.awesomejim.weatherforecast.core.data.utils

import com.awesomejim.weatherforecast.core.data.R
import timber.log.Timber

object WeatherUtils {

    /**
     * Helper method to provide the art resource ID according to the weather condition ID returned
     * by the OpenWeatherMap call. This method is very similar to
     *
     * [.getLargeArtResourceIdForWeatherCondition].
     *
     * The difference between these two methods is that this method provides larger assets, used
     * in the "today view" of the list, as well as in the DetailActivity.
     *
     * @param weatherId from OpenWeatherMap API response
     * See http://openweathermap.org/weather-conditions for a list of all IDs
     *
     * @return resource ID for the corresponding icon. -1 if no relation is found.
     */
    fun iconIdForWeatherCondition(weatherId: Int): Int {

        /*
         * Based on weather code data for Open Weather Map.
         */
        when (weatherId) {
            in 200..232 -> {
                return R.drawable.art_storm
            }
            in 300..321 -> {
                return R.drawable.art_light_rain
            }
            in 500..504 -> {
                return R.drawable.art_rain
            }
            511 -> {
                return R.drawable.art_snow
            }
            in 520..531 -> {
                return R.drawable.art_rain
            }
            in 600..622 -> {
                return R.drawable.art_snow
            }
            in 701..761 -> {
                return R.drawable.art_fog
            }
            762, 771, 781 -> {
                return R.drawable.art_storm
            }
            800 -> {
                return R.drawable.art_clear
            }
            801 -> {
                return R.drawable.art_light_clouds
            }
            in 802..804 -> {
                return R.drawable.art_clouds
            }
            in 900..906 -> {
                return R.drawable.art_storm
            }
            in 958..962 -> {
                return R.drawable.art_storm
            }
            in 951..957 -> {
                return R.drawable.art_clear
            }
            else -> {
                Timber.e("Unknown Weather: %s", weatherId)
                return R.drawable.art_storm
            }
        }
    }
}
