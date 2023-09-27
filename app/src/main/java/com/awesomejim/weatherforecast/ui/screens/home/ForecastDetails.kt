package com.awesomejim.weatherforecast.ui.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.data.model.ForecastMoreDetails
import com.awesomejim.weatherforecast.data.model.HourlyWeatherData
import com.awesomejim.weatherforecast.ui.theme.WeatherForecastTheme
import com.awesomejim.weatherforecast.utilities.SampleData

@Composable
fun ForecastMoreDetails(
    forecastMoreDetails: ForecastMoreDetails,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 2.dp, vertical = 2.dp).fillMaxWidth()
    )
    {
        Column(modifier = modifier.padding(horizontal = 2.dp, vertical = 2.dp)) {
            ConditionsLabelSection(modifier, R.drawable.ic_wind, R.string.wind_label)
            ConditionsLabelSection(modifier, R.drawable.ic_humidity, R.string.humidity_label)
            ConditionsLabelSection(modifier, R.drawable.ic_visibility, R.string.visibility_label)
            ConditionsLabelSection(modifier, R.drawable.ic_pressure, R.string.pressure_label)
        }
        Column(modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp)) {
            Text(
                text = forecastMoreDetails.windDetails,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
            Text(
                text = forecastMoreDetails.humidityDetails,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
            Text(
                text = forecastMoreDetails.visibilityDetails,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
            Text(
                text = forecastMoreDetails.pressureDetails,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun HourlyDataElement(
    hourlyWeatherData: HourlyWeatherData,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 1.dp, vertical = 4.dp)
    ) {
        Text(
            text = hourlyWeatherData.temperature,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Image(
            painter = painterResource(hourlyWeatherData.drawableIcon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
        )
        Text(
            text = hourlyWeatherData.hourTime,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun HourlyDataElementRow(
    hourlyWeatherData: List<HourlyWeatherData>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 4.dp),
        modifier = modifier.background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        items(hourlyWeatherData) { item ->
            HourlyDataElement(item)
        }
    }

}

@Composable
fun ForecastMoreDetailsSection(
    forecastMoreDetails: ForecastMoreDetails,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 1.dp, vertical = 4.dp).fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        ForecastMoreDetails(forecastMoreDetails)
        HourlyDataElementRow(forecastMoreDetails.hourlyWeatherData)
    }

}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun HourlyDataElementPreview() {
    WeatherForecastTheme {
        val hourlyWeatherData = HourlyWeatherData(
            temperature = "23*",
            drawableIcon = R.drawable.art_light_rain,
            hourTime = "16:00"

        )
        HourlyDataElement(
            hourlyWeatherData = hourlyWeatherData,
            modifier = Modifier
        )
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun ForecastMoreDetailsPreview() {
    WeatherForecastTheme {
        val forecastMoreDetails = ForecastMoreDetails(
            windDetails = "dolore",
            humidityDetails = "malorum",
            visibilityDetails = "libero",
            pressureDetails = "eloquentiam",
            emptyList()
        )
        ForecastMoreDetails(
            forecastMoreDetails
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun HourlyDataElementRowPreview() {
    WeatherForecastTheme {
        HourlyDataElementRow(hourlyWeatherData = SampleData.hourlyWeatherDataList)
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(showBackground = true,
    backgroundColor = 0xFFF0EAE2,
    showSystemUi = true)
@Composable
fun ForecastMoreDetailsSectionPreview() {
    WeatherForecastTheme {
        ForecastMoreDetailsSection(SampleData.forecastMoreDetails)
    }
}