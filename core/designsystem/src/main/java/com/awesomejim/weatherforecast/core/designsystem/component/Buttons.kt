package com.awesomejim.weatherforecast.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.awesomejim.weatherforecast.core.designsystem.R

@Composable
fun ConfirmButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Text(text = stringResource(R.string.settings_confirm))
    }
}

@Composable
fun SettingOptionRadioButton(
    text: String,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Row(
        Modifier
            .padding(16.dp)
            .selectable(
                selected = (text == selectedOption),
                onClick = { onOptionSelected(text) }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = (text == selectedOption),
            onClick = null
        )
        BodyText(
            text = text, modifier = Modifier.padding(start = 8.dp),
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
