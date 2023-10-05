package com.awesomejim.weatherforecast.ui.screens.home

import android.content.res.Configuration
import android.graphics.PointF
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
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
        modifier = modifier
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .fillMaxWidth()
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
    val lineColor = MaterialTheme.colorScheme.primary
    Spacer(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxSize()
            .drawWithCache {
                val path = generateSmoothPath(hourlyWeatherData, size)
                val filledPath = Path()
                filledPath.addPath(path)
                filledPath.relativeLineTo(0f, size.height)
                filledPath.lineTo(0f, size.height)
                filledPath.close()
                onDrawBehind {
                    drawPath(path, lineColor, style = Stroke(2.dp.toPx()))

                    drawPath(
                        filledPath,
                        brush = Brush.verticalGradient(
                            listOf(
                                lineColor.copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        ),
                        style = Fill
                    )
                }
            }
    )




}

fun generateSmoothPath(hourlyWeatherData: List<HourlyWeatherData>, size: Size): Path {
    val path = Path()
    val numberEntries = hourlyWeatherData.size - 1
    val weekWidth = size.width / numberEntries

    val max = hourlyWeatherData.maxBy { it.temperatureFloat }
    val min = hourlyWeatherData.minBy { it.temperatureFloat } // will map to x= 0, y = height
    val range = max.temperatureFloat - min.temperatureFloat
    val heightPxPerTemp = size.height / range

    var previousTempX = 0f
    var previousTempY = size.height
    hourlyWeatherData.forEachIndexed { i, hourlyData ->
        if (i == 0) {
            path.moveTo(
                0f,
                size.height - (hourlyData.temperatureFloat - min.temperatureFloat) *
                        heightPxPerTemp
            )

        }
        val tempX = i * weekWidth
        val tempY = size.height - (hourlyData.temperatureFloat - min.temperatureFloat) *
                heightPxPerTemp
        // to do smooth curve graph - we use cubicTo, uncomment section below for non-curve
        val controlPoint1 = PointF((tempX + previousTempX) / 2f, previousTempY)
        val controlPoint2 = PointF((tempX + previousTempX) / 2f, tempY)
        path.cubicTo(
            controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y,
            tempX, tempY
        )
        previousTempX = tempX
        previousTempY = tempY
    }
    return path
}



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
        modifier = modifier
            .padding(horizontal = 1.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        ForecastMoreDetails(forecastMoreDetails)
        LineChart(
            modifier = modifier
                .height(50.dp)
                .fillMaxWidth(), forecastMoreDetails.hourlyWeatherData
        )
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
