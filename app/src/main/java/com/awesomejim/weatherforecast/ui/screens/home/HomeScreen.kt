package com.awesomejim.weatherforecast.ui.screens.home

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.core.designsystem.component.ConditionsLabelSection
import com.awesomejim.weatherforecast.core.designsystem.component.ErrorTextWithAction
import com.awesomejim.weatherforecast.core.designsystem.component.ExpandItemButton
import com.awesomejim.weatherforecast.core.designsystem.component.LoadingProgressScreens
import com.awesomejim.weatherforecast.core.designsystem.component.Subtitle
import com.awesomejim.weatherforecast.core.designsystem.component.SubtitleSmall
import com.awesomejim.weatherforecast.core.getDate
import com.awesomejim.weatherforecast.core.getUpdatedOnDate

@Composable
private fun CurrentWeatherWidget(currentWeather: com.awesomejim.weatherforecast.core.LocationItemData, modifier: Modifier) {
    val drawable = com.awesomejim.weatherforecast.core.data.utils.WeatherUtils
        .iconIdForWeatherCondition(currentWeather.locationWeatherInfo.weatherConditionId)
//    val animateTween by rememberInfiniteTransition(label = "").animateFloat(
//        initialValue = -1f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            tween(2000, easing = LinearEasing),
//            RepeatMode.Reverse
//        ), label = ""
//    )
    //modifier = Modifier.offset(0.dp, (-5).dp * animateTween)
    val lastUpdatedOn = getUpdatedOnDate(currentWeather.locationDataLastUpdate)
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(horizontal = 2.dp, vertical = 2.dp)
            ) {
                Image(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(16.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
                )
                Subtitle(
                    text = currentWeather.locationName
                )
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = modifier
                        .padding(16.dp)
                        .height(100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val temp = stringResource(
                        id = R.string.format_temperature,
                        currentWeather.locationWeatherInfo.weatherTemp
                    )
                    val highLow = stringResource(
                        id = R.string.format_high_low_temperature,
                        currentWeather.locationWeatherInfo.weatherTempMax,
                        currentWeather.locationWeatherInfo.weatherTempMin
                    )
                    Text(
                        text = temp,
                        style = MaterialTheme.typography.displayMedium.copy(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.3f),
                                offset = Offset(3f, 3f),
                                blurRadius = 6f
                            )
                        )

                    )
                    Spacer(modifier = Modifier.weight(1f))
                    SubtitleSmall(
                        text = highLow,
                        modifier = modifier.padding(bottom = 8.dp)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.padding(8.dp).height(100.dp).fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = drawable),
                        contentDescription =
                        currentWeather
                            .locationWeatherInfo.weatherConditionDescription,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Subtitle(
                        text = currentWeather.locationWeatherInfo.weatherConditionDescription,
                        modifier = modifier.padding(bottom = 8.dp).fillMaxWidth()
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()){
                Text(
                    text = "As at:",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
                Text(
                    text = lastUpdatedOn,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = modifier.padding(horizontal = 2.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun ConditionsSection(
    conditionText: String,
    @StringRes conditionLabel: Int,
    @DrawableRes drawable: Int,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(180.dp)
                .height(60.dp)
        ) {
            Text(
                text = conditionText,
                style = MaterialTheme.typography.headlineSmall,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp)
            )
            ConditionsLabelSection(modifier, drawable, conditionLabel)
        }
    }
}



@Composable
fun OtherConditionsSection(
    weatherDetails: com.awesomejim.weatherforecast.core.WeatherStatusInfo,
    modifier: Modifier = Modifier
) {
    val humidity =
        stringResource(id = R.string.format_humidity, weatherDetails.weatherHumidity.toFloat())
    val visibility = "${weatherDetails.weatherVisibility / 1000} km"

    /****************************
     * Wind speed and direction *
     ****************************/
    /* Read wind speed (in MPH) and direction (in compass degrees) from the cursor  */

    val windDirection: String =
        com.awesomejim.weatherforecast.core.data.source.mapper.getFormattedWind(weatherDetails.weatherWindDegrees)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContentScreen(
    currentWeather: com.awesomejim.weatherforecast.core.LocationItemData,
    forecastItem: List<com.awesomejim.weatherforecast.core.LocationItemData>?,
    modifier: Modifier = Modifier
) {
//    var bgColor: Color = grey_rainy
//    var bgImage: Int = R.drawable.sea_sunnypng
//    when (currentWeather.locationWeatherInfo.weatherConditionId) {
//        in 801..804 -> {
//            bgColor = grey_cloudy
//            bgImage = R.drawable.sea_cloudy
//        }
//
//        in 799..800 -> {
//            bgColor = greenish_sunny
//            bgImage = R.drawable.sea_sunnypng
//        }
//
//        in 500..531 -> {
//            bgColor = grey_rainy
//            bgImage = R.drawable.sea_rainy
//        }
//    }

    // Holds the data that is currently expanded to show its moredetails.
    var expandedDate by remember { mutableStateOf<com.awesomejim.weatherforecast.core.LocationItemData?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
    ) {
        item {
            CurrentWeatherWidget(
                currentWeather,
                modifier = Modifier
            )
            OtherConditionsSection(
                weatherDetails = currentWeather.locationWeatherInfo,
                modifier = Modifier.padding(8.dp)
            )
            Divider(thickness = 1.dp)
            Subtitle(
                text = stringResource(id = R.string.home_weekly_forecast_title)

            )
        }
        forecastItem?.let { forecast ->
            if (forecast.isNotEmpty()) {
                items(forecastItem) { item ->

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
                        drawable =
                        com.awesomejim.weatherforecast.core.data.utils.WeatherUtils
                            .iconIdForWeatherCondition(item.locationWeatherInfo.weatherConditionId),
                        forecastMoreDetailsItem = item.forecastMoreDetails,
                        expanded = expandedDate == item,
                        onClick = {
                            expandedDate = if (expandedDate == item) null else item
                        },
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            } else {
                item {
                    LoadingProgressScreens()
                }
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
    forecastMoreDetailsItem: com.awesomejim.weatherforecast.core.ForecastMoreDetailsItem?,
    @DrawableRes drawable: Int,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .animateContentSize()
            .semantics {
                toggleableState = if (expanded) ToggleableState.On else ToggleableState.Off
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(vertical = 4.dp, horizontal = 4.dp)
                .clickable {
                    onClick()
                }
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = modifier.padding(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Text(
                    text = forecastDate,
                    style = MaterialTheme.typography.bodyLarge,
                    color =  MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = conditionText,
                    style = MaterialTheme.typography.bodySmall,
                    color =  MaterialTheme.colorScheme.onSurface
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
                    style = MaterialTheme.typography.labelLarge,
                    color =  MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tempLow,
                    style = MaterialTheme.typography.labelSmall,
                    color =  MaterialTheme.colorScheme.onSurface
                )
            }
            ExpandItemButton(
                expanded = expanded,
                onClick = onClick
            )
        }
        if (expanded) {
            forecastMoreDetailsItem?.let {
                ForecastMoreDetailsSection(forecastMoreDetailsItem)
            }
        }
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    name = "Conditions Section Preview",
    showBackground = true, backgroundColor = 0xFFF0EAE2,
    showSystemUi = false
)
@Composable
fun ConditionsSectionPreview() {
    com.awesomejim.weatherforecast.core.designsystem.theme.WeatherForecastTheme {
        ConditionsSection(
            conditionText = "1km/h SE",
            conditionLabel = R.string.wind_label,
            drawable = R.drawable.ic_wind
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    name = "Other Conditions Section Preview",
    showBackground = true, backgroundColor = 0xFFF0EAE2,
    showSystemUi = false
)
@Composable
fun OtherConditionsSectionPreview() {
    com.awesomejim.weatherforecast.core.designsystem.theme.WeatherForecastTheme {
        val locationItemData =
            com.awesomejim.weatherforecast.core.data.utils.SampleData.sampleLocationItemData
        OtherConditionsSection(
            weatherDetails = locationItemData.locationWeatherInfo,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun ErrorScreen(errorMsgId: Int, onTryAgainClicked: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        ErrorTextWithAction(
            errorMessageId = errorMsgId,
            modifier = Modifier.padding(16.dp)
        ) {
            onTryAgainClicked()
        }
        Spacer(modifier = Modifier.Companion.weight(0.5f))
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    name = "Forecast Item Preview",
    showBackground = true, backgroundColor = 0xFFF0EAE2,
    showSystemUi = false
)
@Composable
fun ForecastItemPreview() {
    com.awesomejim.weatherforecast.core.designsystem.theme.WeatherForecastTheme {
        ForecastItem(
            conditionText = "Partly Sunny",
            forecastDate = "Tuesday, 24 Sep 2023",
            tempHigh = "29*",
            tempLow = "12*",
            drawable = R.drawable.art_clear,
            forecastMoreDetailsItem = com.awesomejim.weatherforecast.core.data.utils.SampleData.forecastMoreDetailsItem,
            expanded = false,
            onClick = {},
            modifier = Modifier.padding(0.dp)
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    name = "Home Screen Preview",
    showBackground = true,
    showSystemUi = false
)
@Composable
fun HomeContentScreenPreview() {
    com.awesomejim.weatherforecast.core.designsystem.theme.WeatherForecastTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val locationItemData =
                com.awesomejim.weatherforecast.core.data.utils.SampleData.sampleLocationItemData
            HomeContentScreen(
                locationItemData,
                com.awesomejim.weatherforecast.core.data.utils.SampleData.foreCastList
            )
        }
    }
}
