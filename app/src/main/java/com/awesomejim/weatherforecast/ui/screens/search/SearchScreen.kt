package com.awesomejim.weatherforecast.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.ui.components.DialogSearchSuccess
import com.awesomejim.weatherforecast.ui.theme.WeatherForecastTheme
import com.awesomejim.weatherforecast.utilities.WeatherUtils

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel
) {
//    val currentWeatherUiState = searchViewModel
//        .searchWeatherUiState
//        .collectAsStateWithLifecycle().value
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
                .background(MaterialTheme.colorScheme.background)
            //.wrapContentSize(Alignment.Center)
        ) {
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

    }
}

@Composable
fun SearchBar(
    searchTerm: String,
    isSearchWordValid: Boolean,
    isSearching:Boolean,
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