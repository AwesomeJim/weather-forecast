package com.awesomejim.weatherforecast.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.ui.CurrentWeatherUiState
import com.awesomejim.weatherforecast.ui.components.DialogSearchSuccess
import com.awesomejim.weatherforecast.ui.screens.home.ErrorScreen
import com.awesomejim.weatherforecast.ui.theme.WeatherForecastTheme
import com.awesomejim.weatherforecast.utilities.WeatherUtils
import timber.log.Timber

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel
) {
    val currentWeatherUiState = searchViewModel
        .searchWeatherUiState
        .collectAsStateWithLifecycle().value

    val searchUiState = searchViewModel
        .searchUiState
        .collectAsStateWithLifecycle().value
    val openAlertDialog = remember { mutableStateOf(false) }
    val locationItemData = remember { mutableStateOf<LocationItemData?>(null) }
    val weatherIcon = remember { mutableIntStateOf(R.drawable.art_clear) }
    when (currentWeatherUiState) {
        is CurrentWeatherUiState.Loading -> {
            Timber.tag("keyboardActions").e("$currentWeatherUiState")
        }

        is CurrentWeatherUiState.Error -> {
            ErrorScreen(
                currentWeatherUiState.errorMessageId,
                onTryAgainClicked = {

                })
        }

        is CurrentWeatherUiState.Success -> {
            Timber.tag("keyboardActions").e("$currentWeatherUiState")
            val data = currentWeatherUiState.currentWeather
            locationItemData.value = data
            val drawable =
                WeatherUtils.
                getLargeArtResourceIdForWeatherCondition(data.locationWeatherInfo.weatherConditionId)
            weatherIcon.intValue = drawable
            openAlertDialog.value = true
        }
    }
    if (openAlertDialog.value) {
        locationItemData.value?.let { data ->
            DialogSearchSuccess(
                onDismissRequest = {
                    openAlertDialog.value = false
                },
                onConfirmation = {
                    openAlertDialog.value = false
                },
                conditionIcon = weatherIcon.intValue,
                locationItemData = data
            )
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.search_screen_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                backgroundColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = contentPadding)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .wrapContentSize(Alignment.Center)
        ) {
            Spacer(Modifier.height(16.dp))
            SearchBar(
                onSearchTermChanged = { searchViewModel.updateUserSearchKeyWord(it) },
                onKeyboardDone = { searchViewModel.fetchForecastCurrentWeatherData() },
                modifier = Modifier.padding(horizontal = 16.dp),
                searchTerm = searchUiState.searchKeyWord
            )
        }

    }
}

@Composable
fun SearchBar(
    searchTerm: String,
    onSearchTermChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    modifier: Modifier = Modifier
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
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun SearchBarPreview() {
    WeatherForecastTheme {
        SearchBar(
            searchTerm = "",
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