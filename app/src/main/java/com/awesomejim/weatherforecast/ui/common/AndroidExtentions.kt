package com.awesomejim.weatherforecast.ui.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import com.awesomejim.weatherforecast.ui.components.PermissionRationaleDialog

@Composable
fun Activity.OnPermissionDenied(
    activityPermissionResult: ActivityResultLauncher<String>,
) {
    val showWeatherUI = remember { mutableStateOf(false) }
    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
        val isDialogShown = remember { mutableStateOf(true) }
        if (isDialogShown.value) {
            PermissionRationaleDialog(
                isDialogShown,
                activityPermissionResult,
                showWeatherUI
            )
        }
    } else {
        activityPermissionResult.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }
}

@Composable
fun Context.CheckForPermissions(
    onPermissionGranted: @Composable () -> Unit,
    onPermissionDenied: @Composable () -> Unit
) {
    when (
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    ) {
        PackageManager.PERMISSION_GRANTED -> {
            onPermissionGranted()
        }
        PackageManager.PERMISSION_DENIED -> {
            onPermissionDenied()
        }
    }
}
