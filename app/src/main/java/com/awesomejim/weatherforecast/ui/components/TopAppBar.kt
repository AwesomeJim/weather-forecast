package com.awesomejim.weatherforecast.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.sp
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.ui.theme.WeatherForecastTheme

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
    refreshButtonState:Boolean = false,
    onRefreshClicked: () -> Unit = {}
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
    WeatherForecastTheme {
        WeatherTopAppBar("Awesome Jim")
    }
}
