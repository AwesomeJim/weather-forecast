package com.awesomejim.weatherforecast.core.designsystem.component

import android.Manifest
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.awesomejim.weatherforecast.core.designsystem.R


@Composable
fun PermissionRationaleDialog(
    isDialogShown: MutableState<Boolean>,
    activityPermissionResult: ActivityResultLauncher<String>,
    showWeatherUI: MutableState<Boolean>
) {
    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
            isDialogShown.value = false
        },
        title = {
            Text(text = stringResource(R.string.location_rationale_title))
        },
        text = {
            Text(text = stringResource(R.string.location_rationale_description))
        },
        confirmButton = {
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

