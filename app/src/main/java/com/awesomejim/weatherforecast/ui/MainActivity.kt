package com.awesomejim.weatherforecast.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.os.Bundle
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
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

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
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.run {
                            mainViewModel.processIntent(
                                MainViewUiState.ReceiveLocation(
                                    longitude = location.longitude,
                                    latitude = location.latitude
                                )
                            )
                        }
                    }
                    .addOnFailureListener { exeption ->
                        // The location is not available
                        // Handle the exception
                    }
               // WeatherAppScreensConfig(navController = rememberNavController())
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