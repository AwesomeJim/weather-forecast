package com.awesomejim.weatherforecast.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.awesomejim.weatherforecast.R

@Composable
fun Subtitle(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified) {
    Text(
        text = text,
        style = MaterialTheme.typography.displayMedium,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 2.dp),
        color = color
    )
}

@Composable
fun SubtitleSmall(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 0.dp),
        color = color
    )
}

@Composable
fun TemperatureHeadline(
    temperature: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    Headline(
        text = temperature,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp),
        color = color,
    )
}

@Composable
fun Headline(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.displayLarge,
        color = color
    )
}

@Composable
fun VersionInfoText(versionInfo: String, modifier: Modifier) {
    Text(
        text = versionInfo,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun BodyText(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        modifier = modifier
    )
}

@Composable
fun ErrorTextWithAction(
    @StringRes errorMessageId: Int,
    modifier: Modifier,
    onTryAgainClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        androidx.compose.material.Text(
            text = stringResource(id = errorMessageId),
            textAlign = TextAlign.Center,
            modifier = modifier.align(Alignment.CenterHorizontally),
            style = androidx.compose.material.MaterialTheme.typography.body1
        )
        Button(
            onClick = { onTryAgainClicked() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            androidx.compose.material.Text(
                text = stringResource(R.string.home_error_try_again),
                style = androidx.compose.material.MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
        }
    }
}
