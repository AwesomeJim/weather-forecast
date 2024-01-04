package com.awesomejim.weatherforecast.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awesomejim.weatherforecast.core.designsystem.R

/**
 * Created by awesome jim on.
 * 29/09/2023
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    canNavigateBack: Boolean = false,
    refreshButtonState: Boolean = false,
    onRefreshClicked: () -> Unit = {},
    mapsButtonState: Boolean = false,
    onMapsClicked: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                text = title,
                fontSize = 22.sp,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier,
        actions = {
            if (refreshButtonState) {
                IconButton(onClick = onRefreshClicked) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Refresh Weather data"
                    )
                }
            }
            if (mapsButtonState) {
                IconButton(onClick = onMapsClicked) {
                    Icon(
                        imageVector = Icons.Filled.Map,
                        contentDescription = "View on the Map"
                    )
                }
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarPreview() {
    com.awesomejim.weatherforecast.core.designsystem.theme.WeatherForecastTheme {
        WeatherTopAppBar("Awesome Jim")
    }
}


/**
 * Shows the floating action button.
 *
 * @param extended Whether the tab should be shown in its expanded state.
 */
@Composable
fun SearchFloatingActionButton(
    extended: Boolean,
    onClick: () -> Unit
) {
    // how it should animate.
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.padding(8.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Map,
                contentDescription = "View On the Map",
                tint = MaterialTheme.colorScheme.onPrimary
            )
            // Toggle the visibility of the content with animation.
            AnimatedVisibility(extended) {
                Text(
                    text = "View On the Map",
                    modifier = Modifier
                        .padding(start = 8.dp, top = 3.dp)
                )
            }
        }
    }
}