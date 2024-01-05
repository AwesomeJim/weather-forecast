package com.awesomejim.weatherforecast.feature.bookmarks

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.awesomejim.weatherforecast.core.ForecastMoreDetailsItem
import com.awesomejim.weatherforecast.core.LocationItemData
import com.awesomejim.weatherforecast.core.data.utils.SampleData
import com.awesomejim.weatherforecast.core.designsystem.component.ConditionsLabelSection
import com.awesomejim.weatherforecast.core.designsystem.component.Subtitle
import com.awesomejim.weatherforecast.core.designsystem.theme.WeatherForecastTheme


/**
 * Created by Awesome Jim on.
 * 04/01/2024
 */

@Composable
fun ForecastMoreDetails(
    forecastMoreDetailsItem: ForecastMoreDetailsItem,
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
                text = forecastMoreDetailsItem.windDetails,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
            Text(
                text = forecastMoreDetailsItem.humidityDetails,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
            Text(
                text = forecastMoreDetailsItem.visibilityDetails,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
            Text(
                text = forecastMoreDetailsItem.pressureDetails,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}
@Composable
fun DialogSearchSuccess(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    @DrawableRes conditionIcon: Int,
    locationItemData: LocationItemData,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        val weatherTempMax = stringResource(
            id = R.string.format_temperature,
            locationItemData.locationWeatherInfo.weatherTempMax
        )
        val weatherTempMin = stringResource(
            id = R.string.format_temperature,
            locationItemData.locationWeatherInfo.weatherTempMin
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inversePrimary,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Subtitle(
                    text = locationItemData.locationName
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = conditionIcon),
                            contentDescription =
                            locationItemData
                                .locationWeatherInfo
                                .weatherConditionDescription,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text =
                            locationItemData
                                .locationWeatherInfo
                                .weatherConditionDescription,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = weatherTempMax,
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = weatherTempMin,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 2.dp, vertical = 2.dp)
                        .fillMaxWidth()
                ) {
                    ForecastMoreDetails(locationItemData.forecastMoreDetailsItem!!)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    OutlinedButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Save Location")
                    }
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun SearchBarPreview2() {
    WeatherForecastTheme {
        DialogSearchSuccess(
            onDismissRequest = {},
            onConfirmation = {},
            conditionIcon = com.awesomejim.weatherforecast.core.data.R.drawable.art_light_clouds,
            locationItemData = SampleData.sampleLocationItemData
        )
    }
}