package com.awesomejim.weatherforecast.ui.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
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
    ) {
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
                color = MaterialTheme.colorScheme.onSurface,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
            Text(
                text = forecastMoreDetails.humidityDetails,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
            Text(
                text = forecastMoreDetails.visibilityDetails,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
            Text(
                text = forecastMoreDetails.pressureDetails,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
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
            color = MaterialTheme.colorScheme.onPrimaryContainer
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
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
/**
 * Render hourly temperature with curve-line chart
 * show with animation
 */
@Composable
fun LineChart(
    modifier: Modifier,
    hourlyWeatherData: List<HourlyWeatherData>
) {
    val zipList: List<Pair<HourlyWeatherData, HourlyWeatherData>> = hourlyWeatherData.zipWithNext()
    Row(modifier = modifier.padding(horizontal = 24.dp)) {
        val max = hourlyWeatherData.maxBy { it.temperatureFloat }.temperatureFloat
        val min = hourlyWeatherData.minBy { it.temperatureFloat }.temperatureFloat

        val lineColor = MaterialTheme.colorScheme.primary

        for (pair in zipList) {

            val fromValuePercentage = getValuePercentageForRange(pair.first.temperatureFloat, max, min)
            val toValuePercentage = getValuePercentageForRange(pair.second.temperatureFloat, max, min)

            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                onDraw = {
                    val fromPoint = Offset(x = 0f, y = size.height.times(1 - fromValuePercentage))
                    val toPoint =
                        Offset(x = size.width, y = size.height.times(1 - toValuePercentage))
                    drawLine(
                        color = lineColor,
                        start = fromPoint,
                        end = toPoint,
                        strokeWidth = 3f
                    )
                })
        }
    }
}

private fun getValuePercentageForRange(value: Float, max: Float, min: Float) =
    (value - min) / (max - min)

@Composable
fun HourlyDataElementRow(
    hourlyWeatherData: List<HourlyWeatherData>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.SpaceEvenly,
        contentPadding = PaddingValues(horizontal = 8.dp),
        modifier = modifier.fillMaxWidth()
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
    ) {
        ForecastMoreDetails(forecastMoreDetails)
        LineChart(Modifier.height(50.dp).fillMaxWidth(), forecastMoreDetails.hourlyWeatherData)
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
@Preview(
    showBackground = true,
    backgroundColor = 0xFFF0EAE2
)
@Composable
fun ForecastMoreDetailsSectionPreview() {
    WeatherForecastTheme {
        ForecastMoreDetailsSection(SampleData.forecastMoreDetails)
    }
}
