package com.awesomejim.weatherforecast.ui.screens.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.material.Text
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.data.model.WeatherStatusInfo
import com.awesomejim.weatherforecast.ui.components.Subtitle
import com.awesomejim.weatherforecast.ui.components.SubtitleSmall
import com.awesomejim.weatherforecast.ui.components.TemperatureHeadline
import com.awesomejim.weatherforecast.ui.theme.WeatherForecastTheme
import com.awesomejim.weatherforecast.utilities.SampleData


@Composable
private fun CurrentWeatherWidget(currentWeather: LocationItemData) {
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
    val humidity = weatherDetails.weatherHumidity.toString()
    val visibility = weatherDetails.weatherVisibility.toString()
    val wind = weatherDetails.weatherWindDegrees.toString()
    val pressure = weatherDetails.weatherPressure.toString()
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
                conditionText = wind,
                conditionLabel = R.string.wind_label,
                drawable = R.drawable.ic_wind
            )

        }
        Spacer(modifier = Modifier
            .width(8.dp)
            .height(8.dp))
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
        CurrentWeatherWidget(weatherDetails)
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

@Composable
fun HomeScreen() {
    Column {
        val locationItemData = SampleData.locationItemData
        WeatherDetailsSection(
            weatherDetails = locationItemData,
            imageResource = R.drawable.sea_sunnypng
        )
        OtherConditionsSection(
            weatherDetails = locationItemData.locationWeatherInfo,
            modifier = Modifier.padding(8.dp)
        )
    }
}



@Preview(
    name = "Home Screen Preview",
    showBackground = true,
    showSystemUi = false
)
@Composable
fun HomeScreenPreview() {
    WeatherForecastTheme {
        HomeScreen()
    }
}