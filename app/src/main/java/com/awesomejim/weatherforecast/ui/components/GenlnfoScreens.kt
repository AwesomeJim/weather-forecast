package com.awesomejim.weatherforecast.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.ui.theme.WeatherForecastTheme

@Composable
fun GeneralInfoScreens(@StringRes message: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = stringResource(message),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun RequiresPermissionsScreen() {
    GeneralInfoScreens(message = R.string.location_no_permission_screen_description)
}

@Composable
fun EnableLocationSettingScreen() {
    GeneralInfoScreens(message = R.string.location_settings_not_enabled)
}

@Preview(showBackground = true)
@Composable
fun RequiresPermissionsScreenPreview() {
    WeatherForecastTheme {
        RequiresPermissionsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun EnableLocationSettingScreenPreview() {
    WeatherForecastTheme {
        EnableLocationSettingScreen()
    }
}
