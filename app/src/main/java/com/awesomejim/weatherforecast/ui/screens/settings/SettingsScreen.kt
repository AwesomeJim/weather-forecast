package com.awesomejim.weatherforecast.ui.screens.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.ui.components.SettingOptionRadioButton
import com.awesomejim.weatherforecast.ui.components.SettingOptionsDialog
import com.awesomejim.weatherforecast.ui.components.VersionInfoText

@Composable
fun SettingsScreen(
    state: SettingsScreenViewState,
    onUnitChanged: (String) -> Unit
) {
    Column(modifier = Modifier) {
        val openUnitSelectionDialog = remember { mutableStateOf(false) }

        SettingOptionRow(
            optionLabel = stringResource(R.string.settings_unit_label),
            optionValue = state.selectedUnit,
            optionIcon = R.drawable.ic_temp_unit,
            optionIconContentDescription =
            stringResource(R.string.settings_content_description_unit_icon),
            modifier = Modifier,
        ) {
            openUnitSelectionDialog.value = openUnitSelectionDialog.value.not()
        }
        if (openUnitSelectionDialog.value) {
            val availableUnits = state.availableUnits
            val (selectedOption, onOptionSelected) =
                remember {
                    mutableStateOf(state.selectedUnit)
                }
            SettingOptionsDialog(
                onDismiss = { openUnitSelectionDialog.value = false },
                onConfirm = {
                    onUnitChanged(selectedOption)
                    openUnitSelectionDialog.value = false
                },
                items = availableUnits,
            ) { unit ->
                SettingOptionRadioButton(
                    text = unit,
                    selectedOption = selectedOption,
                    onOptionSelected = onOptionSelected
                )
            }
        }

        Spacer(modifier = Modifier.weight(1.0f))

        VersionInfoText(
            versionInfo = state.versionInfo,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
fun SettingOptionRow(
    optionLabel: String,
    optionValue: String,
    @DrawableRes optionIcon: Int,
    optionIconContentDescription: String,
    modifier: Modifier = Modifier,
    onOptionClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOptionClicked() }
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Image(
            painter = painterResource(id = optionIcon),
            contentDescription = optionIconContentDescription,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
            modifier = Modifier
                .size(36.dp)
                .padding(8.dp)
        )
        Text(
            text = optionLabel,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.weight(1.0f))
        Text(
            text = optionValue,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(8.dp)
        )
    }
}
