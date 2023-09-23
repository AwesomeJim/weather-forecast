package com.awesomejim.weatherforecast.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.awesomejim.weatherforecast.ui.common.CheckForPermissions
import com.awesomejim.weatherforecast.ui.common.OnPermissionDenied
import com.awesomejim.weatherforecast.ui.components.EnableLocationSettingScreen
import com.awesomejim.weatherforecast.ui.components.LoadingProgressScreens
import com.awesomejim.weatherforecast.ui.components.RequiresPermissionsScreen
import com.awesomejim.weatherforecast.ui.theme.WeatherForecastTheme
import com.awesomejim.weatherforecast.utilities.createLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val locationRequestLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                mainViewModel.processIntent(MainViewUiState.CheckLocationSettings(isEnabled = true))
            } else {
                mainViewModel.processIntent(MainViewUiState.CheckLocationSettings(isEnabled = false))
            }
        }
    private val permissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            mainViewModel.processIntent(MainViewUiState.GrantPermission(isGranted = isGranted))
        }

    // Get location updates.
    val locationRequest = com.google.android.gms.location.LocationRequest.Builder(30_000L)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY) //PRIORITY_BALANCED_POWER_ACCURACY
        .build()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createLocationRequest(
            activity = this@MainActivity,
            locationRequestLauncher = locationRequestLauncher
        ) {
            mainViewModel.processIntent(MainViewUiState.CheckLocationSettings(isEnabled = true))
        }

        // Create an instance of the FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            WeatherForecastTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val state = mainViewModel.state.collectAsState().value
                    Greeting("Android")
                    CheckForPermissions(
                        onPermissionGranted = {
                            mainViewModel.processIntent(MainViewUiState.GrantPermission(isGranted = true))
                        },
                        onPermissionDenied = {
                            OnPermissionDenied(activityPermissionResult = permissionRequestLauncher)
                        }
                    )

                    InitMainScreen(state)
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    @Composable
    private fun InitMainScreen(state: MainViewState) {
        when {
            state.isLocationSettingEnabled && state.isPermissionGranted -> {
                // Get the current location of the device.
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, locationCallback,
                    Looper.myLooper()
                )
//                    .addOnSuccessListener { location: Location? ->
//                        location?.run {
//                            Timber.tag("MainViewModel").e("addOnSuccessListener called")
//                            mainViewModel.processIntent(
//                                MainViewUiState.ReceiveLocation(
//                                    longitude = location.longitude,
//                                    latitude = location.latitude
//                                )
//                            )
//                        }
//                    }
//                    .addOnFailureListener { exeption ->
//                        // The location is not available
//                        // Handle the exception
//                        Timber.tag("MainViewModel").e("addOnFailureListener exeption ${exeption.message}")
//                    }
//                    .addOnCanceledListener {
//                        Timber.tag("MainViewModel").e("addOnCanceledListener ")
//                    }
                // WeatherAppScreensConfig(navController = rememberNavController())
                mainViewModel.testAPiCall()
            }

            state.isLocationSettingEnabled && !state.isPermissionGranted -> {
                RequiresPermissionsScreen()
            }

            !state.isLocationSettingEnabled && !state.isPermissionGranted -> {
                EnableLocationSettingScreen()
            }

            else -> LoadingProgressScreens()
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Timber.tag("MainViewModel").e("onLocationResult called")
            locationResult.lastLocation?.let { location: Location ->
                location.run {
                    // The location is available.
                    // Do something with the location.
                    Timber.tag("MainViewModel").e("addOnSuccessListener called")
                    mainViewModel.processIntent(
                        MainViewUiState.ReceiveLocation(
                            longitude = location.longitude,
                            latitude = location.latitude))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherForecastTheme {
        Greeting("Android")
    }
}

