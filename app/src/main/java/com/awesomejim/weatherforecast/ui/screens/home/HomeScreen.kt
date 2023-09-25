package com.awesomejim.weatherforecast.ui.screens.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.data.model.WeatherStatusInfo
import com.awesomejim.weatherforecast.di.network.getFormattedWind
import com.awesomejim.weatherforecast.ui.CurrentWeatherUiState
import com.awesomejim.weatherforecast.ui.MainViewModel
import com.awesomejim.weatherforecast.ui.common.getDate
import com.awesomejim.weatherforecast.ui.components.LoadingProgressScreens
import com.awesomejim.weatherforecast.ui.components.Subtitle
import com.awesomejim.weatherforecast.ui.components.SubtitleSmall
import com.awesomejim.weatherforecast.ui.components.TemperatureHeadline
import com.awesomejim.weatherforecast.ui.theme.WeatherForecastTheme
import com.awesomejim.weatherforecast.utilities.SampleData
import com.awesomejim.weatherforecast.utilities.WeatherUtils


@Composable
private fun CurrentWeatherWidget(currentWeather: LocationItemData, modifier: Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Subtitle(
            text = currentWeather.locationName,
            color = MaterialTheme.colorScheme.onPrimary
        )
        val temp = stringResource(
            id = R.string.format_temperature,
            currentWeather.locationWeatherInfo.weatherTemp
        )
        val highLow = stringResource(
            id = R.string.format_high_low_temperature,
            currentWeather.locationWeatherInfo.weatherTempMax,
            currentWeather.locationWeatherInfo.weatherTempMin
        )
        TemperatureHeadline(
            temperature = temp,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Subtitle(
            text = currentWeather.locationWeatherInfo.weatherConditionDescription,
            color = MaterialTheme.colorScheme.onPrimary
        )
        SubtitleSmall(
            text = highLow,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Composable
fun ConditionsSection(
    conditionText: String,
    @StringRes conditionLabel: Int,
    @DrawableRes drawable: Int,
    modifier: Modifier = Modifier
) {
    ElevatedCard {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(180.dp)
        ) {
            Text(
                text = conditionText,
                style = MaterialTheme.typography.headlineSmall,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(horizontal = 2.dp, vertical = 2.dp)
            ) {
                Image(
                    painter = painterResource(drawable),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = stringResource(conditionLabel),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)

                )
            }
        }
    }
}


@Composable
fun OtherConditionsSection(
    weatherDetails: WeatherStatusInfo,
    modifier: Modifier = Modifier
) {
    val humidity =
        stringResource(id = R.string.format_humidity, weatherDetails.weatherHumidity.toFloat())
    val visibility = "${weatherDetails.weatherVisibility / 1000} km"

    /****************************
     * Wind speed and direction *
     ****************************/
    /* Read wind speed (in MPH) and direction (in compass degrees) from the cursor  */
    val windSpeed = weatherDetails.weatherWindSpeed

    val windDirection: String = getFormattedWind(weatherDetails.weatherWindDegrees)
    val windString = stringResource(
        id = R.string.format_wind_kmh,
        weatherDetails.weatherWindSpeed.toFloat(),
        windDirection
    )

    val pressure =
        stringResource(id = R.string.format_pressure, weatherDetails.weatherPressure.toFloat())
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Row {
            ConditionsSection(
                conditionText = pressure,
                conditionLabel = R.string.pressure_label,
                drawable = R.drawable.ic_pressure
            )
            Spacer(modifier = Modifier.width(8.dp))
            ConditionsSection(
                conditionText = windString,
                conditionLabel = R.string.wind_label,
                drawable = R.drawable.ic_wind
            )
        }
        Spacer(
            modifier = Modifier
                .width(8.dp)
                .height(8.dp)
        )
        Row {
            ConditionsSection(
                conditionText = visibility,
                conditionLabel = R.string.visibility_label,
                drawable = R.drawable.ic_visibility
            )
            Spacer(modifier = Modifier.width(8.dp))
            ConditionsSection(
                conditionText = humidity,
                conditionLabel = R.string.humidity_label,
                drawable = R.drawable.ic_humidity
            )
        }

    }

}


@Composable
fun WeatherDetailsSection(
    weatherDetails: LocationItemData,
    @DrawableRes imageResource: Int,
    modifier: Modifier = Modifier
) {
    val image = painterResource(imageResource)
    val imageModifier = Modifier
        .heightIn(min = 200.dp, max = 250.dp)
        .fillMaxWidth()
    Box {
        Image(
            painter = image,
            modifier = imageModifier,
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        CurrentWeatherWidget(weatherDetails, modifier = modifier)
    }
}

@Composable
fun HomeContentScreen(
    currentWeather: LocationItemData,
    forecastItem: List<LocationItemData>?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        WeatherDetailsSection(
            weatherDetails = currentWeather,
            imageResource = R.drawable.sea_sunnypng
        )
        OtherConditionsSection(
            weatherDetails = currentWeather.locationWeatherInfo,
            modifier = Modifier.padding(8.dp)
        )
        forecastItem?.let { forecast ->
            if (forecast.isNotEmpty()) {
                ForecastList(forecastItem = forecast)
            } else {
                LoadingProgressScreens()
            }
        }
    }
}


@Composable
fun ForecastItem(
    conditionText: String,
    forecastDate: String,
    tempHigh: String,
    tempLow: String,
    @DrawableRes drawable: Int,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.small,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp).padding(vertical = 4.dp, horizontal = 4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = modifier.padding(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Text(
                    text = forecastDate,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = conditionText,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(drawable),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(48.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            ) {
                Text(
                    text = tempHigh,
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tempLow,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForecastList(forecastItem: List<LocationItemData>) {
    Divider(color = Color.Black, thickness = 1.dp)
    Subtitle(text = stringResource(id = R.string.home_weekly_forecast_title))
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(forecastItem)
        { item ->
            val weatherTempMax = stringResource(
                id = R.string.format_temperature,
                item.locationWeatherInfo.weatherTempMax
            )
            val weatherTempMin = stringResource(
                id = R.string.format_temperature,
                item.locationWeatherInfo.weatherTempMin
            )
            val dateString = getDate(item.locationDataTime, "EEEE, dd-MMM")
            ForecastItem(
                conditionText = item.locationWeatherInfo.weatherConditionDescription,
                forecastDate = dateString,
                tempHigh = weatherTempMax,
                tempLow = weatherTempMin,
                drawable = WeatherUtils.getLargeArtResourceIdForWeatherCondition(item.locationWeatherInfo.weatherConditionId),
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}


@Preview(
    name = "Conditions Section Preview",
    showBackground = true, backgroundColor = 0xFFF0EAE2,
    showSystemUi = false
)
@Composable
fun ConditionsSectionPreview() {
    WeatherForecastTheme {
        ConditionsSection(
            conditionText = "1km/h SE",
            conditionLabel = R.string.wind_label,
            drawable = R.drawable.ic_wind,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(
    name = "Other Conditions Section Preview",
    showBackground = true, backgroundColor = 0xFFF0EAE2,
    showSystemUi = false
)
@Composable
fun OtherConditionsSectionPreview() {
    WeatherForecastTheme {
        val locationItemData = SampleData.locationItemData
        OtherConditionsSection(
            weatherDetails = locationItemData.locationWeatherInfo,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun HomeScreen(mainViewModel: MainViewModel, modifier: Modifier = Modifier) {
    val currentWeatherUiState = mainViewModel
        .currentWeatherUiState
        .collectAsStateWithLifecycle().value
    val forecastListState = mainViewModel
        .forecastListState
        .collectAsStateWithLifecycle().value
    when (currentWeatherUiState) {
        is CurrentWeatherUiState.Loading -> {
            LoadingProgressScreens()
        }

        is CurrentWeatherUiState.Error -> {

        }

        is CurrentWeatherUiState.Success -> {
            HomeContentScreen(
                currentWeatherUiState.currentWeather,
                forecastListState,
                modifier
            )
        }
    }
}


@Preview(
    name = "Weather Details Section Preview",
    showBackground = true,
    showSystemUi = false
)
@Composable
fun WeatherDetailsSectionPreview() {
    WeatherForecastTheme {
        val locationItemData = SampleData.locationItemData
        WeatherDetailsSection(
            weatherDetails = locationItemData,
            imageResource = R.drawable.sea_sunnypng
        )
    }
}


@Preview(
    name = "Forecast Item Preview",
    showBackground = true, backgroundColor = 0xFFF0EAE2,
    showSystemUi = false
)
@Composable
fun ForecastItemPreview() {
    WeatherForecastTheme {
        ForecastItem(
            conditionText = "Partly Sunny",
            forecastDate = "Tuesday, 24 Sep 2023",
            tempHigh = "29*",
            tempLow = "12*",
            drawable = R.drawable.art_clear,
            modifier = Modifier.padding(0.dp)
        )
    }
}

@Preview(
    name = "Home Screen Preview",
    showBackground = true,
    showSystemUi = false
)
@Composable
fun HomeContentScreenPreview() {
    WeatherForecastTheme {
        val locationItemData = SampleData.locationItemData
        HomeContentScreen(locationItemData, SampleData.foreCastList)
    }
}