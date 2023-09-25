package com.awesomejim.weatherforecast.ui.components

import android.Manifest
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.ui.screens.home.ForecastMoreDetails
import com.awesomejim.weatherforecast.ui.theme.WeatherForecastTheme
import com.awesomejim.weatherforecast.utilities.SampleData


@Composable
fun PermissionRationaleDialog(
    isDialogShown: MutableState<Boolean>,
    activityPermissionResult: ActivityResultLauncher<String>,
    showWeatherUI: MutableState<Boolean>
) {
    AlertDialog(
        onDismissRequest = { isDialogShown.value = false },
        title = {
            Text(text = stringResource(R.string.location_rationale_title))
        },
        text = {
            Text(text = stringResource(R.string.location_rationale_description))
        },
        buttons = {
            Button(onClick = {
                isDialogShown.value = false
                activityPermissionResult.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }) {
                Text(text = stringResource(R.string.location_rationale_button_grant))
            }
            Button(onClick = {
                isDialogShown.value = false
                showWeatherUI.value = false
            }) {
                Text(text = stringResource(R.string.location_rationale_button_deny))
            }
        }
    )
}

@Composable
fun <T> SettingOptionsDialog(
    items: List<T>,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable (T) -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        LazyColumn(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onSecondary)
                .padding(16.dp)
        ) {
            items(items) { item ->
                content(item)
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    ConfirmButton(
                        modifier = Modifier
                            .padding(16.dp),
                        onClick = {
                            onConfirm()
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }

            }
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
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Subtitle(
                    text = locationItemData.locationName,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = conditionIcon),
                            contentDescription = locationItemData.locationWeatherInfo.weatherConditionDescription,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = locationItemData.locationWeatherInfo.weatherConditionDescription,
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
                    ForecastMoreDetails(locationItemData.forecastMoreDetails!!)
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





@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun SearchBarPreview() {
    WeatherForecastTheme {
        DialogSearchSuccess(
            onDismissRequest = {},
            onConfirmation = {},
            conditionIcon = R.drawable.art_light_clouds,
            locationItemData = SampleData.locationItemData
        )

    }
}