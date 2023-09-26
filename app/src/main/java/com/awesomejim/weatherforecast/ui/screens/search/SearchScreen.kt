package com.awesomejim.weatherforecast.ui.screens.search

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.ui.common.getUpdatedOnDate
import com.awesomejim.weatherforecast.ui.components.DialogSearchSuccess
import com.awesomejim.weatherforecast.ui.components.DismissBackground
import com.awesomejim.weatherforecast.ui.components.Subtitle
import com.awesomejim.weatherforecast.ui.components.SubtitleSmall
import com.awesomejim.weatherforecast.ui.components.TemperatureHeadline
import com.awesomejim.weatherforecast.ui.screens.home.ExpandItemButton
import com.awesomejim.weatherforecast.ui.screens.home.ForecastMoreDetails
import com.awesomejim.weatherforecast.ui.theme.WeatherForecastTheme
import com.awesomejim.weatherforecast.utilities.SampleData
import com.awesomejim.weatherforecast.utilities.WeatherUtils
import kotlinx.coroutines.delay
import timber.log.Timber

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel
) {
    val savedLocationListUiState = searchViewModel
        .savedLocationListUiState
        .collectAsStateWithLifecycle().value
    //
    val searchUiState = searchViewModel
        .searchUiState
        .collectAsStateWithLifecycle().value
    //
    if (searchUiState.isSearchingSuccessful) {
        searchUiState.searchResultWeatherData?.let { searchResults ->
            val weatherIcon =
                WeatherUtils.getLargeArtResourceIdForWeatherCondition(searchResults.locationWeatherInfo.weatherConditionId)
            DialogSearchSuccess(
                onDismissRequest = {
                    searchViewModel.updateSearchStatus()
                },
                onConfirmation = {
                    searchViewModel.saveLocation(searchResults)
                    searchViewModel.updateSearchStatus()
                },
                conditionIcon = weatherIcon,
                locationItemData = searchResults
            )
        }
    }

    if (searchUiState.isSearchError) {
        searchUiState.searchErrorMessage?.let { errorMessageId ->
            AlertDialogError(
                onDismissRequest = {
                    searchViewModel.updateSearchStatus()
                },
                onConfirmation = {
                    searchViewModel.updateSearchStatus()
                },
                dialogTitle = "Request Failed",
                dialogText = stringResource(id = errorMessageId)
            )
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.saved_screen_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                backgroundColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                Spacer(Modifier.height(16.dp))
                SearchBar(
                    searchTerm = searchUiState.searchKeyWord,
                    isSearchWordValid = searchUiState.isSearchWordValid,
                    onSearchTermChanged = {
                        searchViewModel.updateUserSearchKeyWord(it)
                    },
                    onKeyboardDone = {
                        if (searchUiState.isSearchWordValid) {
                            searchViewModel.fetchForecastCurrentWeatherData()
                        }
                    },
                    isSearching = searchUiState.isSearching,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            if (savedLocationListUiState.itemList.isNotEmpty()) {
                itemsIndexed(
                    items = savedLocationListUiState.itemList,
                    // Provide a unique key based on the item  content, for our case we use locationId
                    key = { _, item -> item.locationId }
                ) { _, location ->
                    val drawable =
                        WeatherUtils.getLargeArtResourceIdForWeatherCondition(location.locationWeatherInfo.weatherConditionId)
                    EditableLocationItem(
                        locationItemData = location,
                        conditionIcon = drawable,
                        onRefresh = { locationItemData ->
                            Timber.e("dismissValue onRemove ${locationItemData.locationId}")
                            searchViewModel.refreshWeatherData(locationItemData)
                        },
                        onRemove = { locationItemData ->
                            Timber.e("dismissValue onRemove ${locationItemData.locationId}")
                            searchViewModel.deleteLocationItem(locationItemData)
                        })
                }
            }
        }

    }
}

/**
 * Composable representing an email item with swipe-to-dismiss functionality.
 *
 * @param locationItemData The location message to display.
 * @param onRemove Callback invoked when the location item is dismissed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableLocationItem(
    locationItemData: LocationItemData,
    @DrawableRes conditionIcon: Int,
    onRefresh: (LocationItemData) -> Unit,
    onRemove: (LocationItemData) -> Unit
) {
    val context = LocalContext.current
    var show by remember { mutableStateOf(true) }
   // var dismissValue by remember { mutableStateOf<DismissValue>(DismissValue.Default) }
    val currentItem by rememberUpdatedState(locationItemData)
    val dismissState = rememberDismissState(
        confirmValueChange = {
           // dismissValue = it
            if (it == DismissValue.DismissedToStart) {
                onRefresh(currentItem)
            }
            if (it == DismissValue.DismissedToEnd) {
                show = false
                true
            } else false
        },
        positionalThreshold = {
            150.dp.toPx()
        }
    )
    AnimatedVisibility(
        show, exit = fadeOut(spring())
    ) {
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier,
            background = {
                DismissBackground(dismissState)
            },
            dismissContent = {
                SavedLocationItem(
                    conditionIcon = conditionIcon,
                    locationItemData = locationItemData
                )
            }
        )
    }
    LaunchedEffect(show) {
        if (!show) {
            delay(800)
            onRemove(currentItem)
            Toast.makeText(context, "Location removed", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun SearchBar(
    searchTerm: String,
    isSearchWordValid: Boolean,
    isSearching: Boolean,
    onSearchTermChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        TextField(
            value = searchTerm,
            onValueChange = onSearchTermChanged,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            isError = searchTerm.isNotEmpty() && !isSearchWordValid,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colorScheme.surface
            ),
            placeholder = {
                Text(stringResource(R.string.placeholder_search))
            },
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onKeyboardDone()
                }
            )
        )
        if (searchTerm.isNotEmpty() && !isSearchWordValid) {
            Text(text = "Please enter valid text", color = MaterialTheme.colorScheme.error)
        }
        if (isSearching) {
            CircularProgressIndicator(
                modifier = Modifier.width(34.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                trackColor = MaterialTheme.colorScheme.primary,
            )
        }
    }
}


@Composable
fun SavedLocationItem(
    @DrawableRes conditionIcon: Int,
    locationItemData: LocationItemData,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val temp = stringResource(
        id = R.string.format_temperature,
        locationItemData.locationWeatherInfo.weatherTemp
    )
    val highLow = stringResource(
        id = R.string.format_high_low_temperature,
        locationItemData.locationWeatherInfo.weatherTempMax,
        locationItemData.locationWeatherInfo.weatherTempMin
    )
    val lastUpdatedOn = getUpdatedOnDate(locationItemData.locationDataLastUpdate)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(horizontal = 2.dp, vertical = 0.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = modifier
                        .padding(2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TemperatureHeadline(
                        temperature = temp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    SubtitleSmall(
                        text = highLow,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Image(
                        painter = painterResource(id = conditionIcon),
                        contentDescription = locationItemData.locationWeatherInfo.weatherConditionDescription,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(48.dp)
                    )
                    Subtitle(
                        text = locationItemData.locationWeatherInfo.weatherConditionDescription,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

            }
            Row {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = modifier.padding(horizontal = 0.dp, vertical = 0.dp)
                ) {
                    Subtitle(
                        text = locationItemData.locationName,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Row {
                        Text(
                            text = "Updated on:",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                        )
                        Text(
                            text = lastUpdatedOn,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = modifier.padding(horizontal = 2.dp, vertical = 2.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                ExpandItemButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded }
                )
            }
            if (expanded) {
                locationItemData.forecastMoreDetails?.let { data ->
                    Divider(color = MaterialTheme.colorScheme.onTertiaryContainer, thickness = 1.dp)
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = modifier.padding(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        ForecastMoreDetails(data)
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun SavedLocationItemPreview() {
    WeatherForecastTheme {
        SavedLocationItem(
            conditionIcon = R.drawable.art_light_clouds,
            locationItemData = SampleData.sampleLocationItemData
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun SearchBarPreview() {
    WeatherForecastTheme {
        SearchBar(
            searchTerm = "",
            isSearchWordValid = true,
            isSearching = true,
            onSearchTermChanged = {},
            onKeyboardDone = {},
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun AlertDialogError(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    AlertDialog(
        icon = {
            Icon(Icons.Filled.ErrorOutline, contentDescription = dialogTitle)
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Try Again")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun AlertDialogPreview() {
    WeatherForecastTheme {
        AlertDialogError(
            onDismissRequest = { },
            onConfirmation = {
                println("Confirmation registered") // Add logic here to handle confirmation.
            },
            dialogTitle = "Alert dialog example",
            dialogText = "This is an example of an alert dialog with buttons."
        )

    }
}