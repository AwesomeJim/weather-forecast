package com.awesomejim.weatherforecast.ui.screens.main

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.awesomejim.weatherforecast.R
import com.awesomejim.weatherforecast.ui.common.CheckForPermissions
import com.awesomejim.weatherforecast.ui.common.OnPermissionDenied
import com.awesomejim.weatherforecast.ui.components.EnableLocationSettingScreen
import com.awesomejim.weatherforecast.ui.components.LoadingProgressScreens
import com.awesomejim.weatherforecast.ui.components.RequiresPermissionsScreen
import com.awesomejim.weatherforecast.ui.components.SearchFloatingActionButton
import com.awesomejim.weatherforecast.ui.components.WeatherTopAppBar
import com.awesomejim.weatherforecast.ui.nav.AppBottomNavigationItem
import com.awesomejim.weatherforecast.ui.nav.BottomNavItem
import com.awesomejim.weatherforecast.ui.nav.NavigationGraph
import com.awesomejim.weatherforecast.ui.theme.WeatherForecastTheme
import com.awesomejim.weatherforecast.utilities.createLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
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
                mainViewModel.processIntent(
                    MainViewUiState
                        .CheckLocationSettings(isEnabled = true)
                )
            } else {
                mainViewModel
                    .processIntent(
                        MainViewUiState
                            .CheckLocationSettings(isEnabled = false)
                    )
            }
        }
    private val permissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            mainViewModel.processIntent(MainViewUiState.GrantPermission(isGranted = isGranted))
        }

    // Get location updates.
    private val locationRequest = LocationRequest.Builder(30_000L)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY) // PRIORITY_BALANCED_POWER_ACCURACY
        .build()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val bottomNavigationItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Settings
    )
    private lateinit var navController: NavHostController

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
                navController = rememberNavController()
                val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
                val title = rememberSaveable { (mutableStateOf("")) }
                val canNavigateBackState = rememberSaveable { (mutableStateOf(false)) }
                val refreshButtonState = rememberSaveable { (mutableStateOf(false)) }
                val mapsButtonState = rememberSaveable { (mutableStateOf(false)) }
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                when (navBackStackEntry?.destination?.route) {
                    BottomNavItem.LocationPhotos.routeWithArgs -> {
                        // Retrieve the passed argument
                        title.value = navBackStackEntry!!
                            .arguments?.getString(
                                BottomNavItem
                                    .LocationPhotos
                                    .locationNameTypeArg
                            ) ?: "Location Photos"
                        canNavigateBackState.value = true
                        bottomBarState.value = false
                        if (refreshButtonState.value) refreshButtonState.value = false
                        if (mapsButtonState.value) mapsButtonState.value = false
                    }

                    BottomNavItem.Home.screenRoute -> {
                        title.value =
                            stringResource(id = R.string.home_title_currently)
                        refreshButtonState.value = true
                        if (!bottomBarState.value) bottomBarState.value = true
                        if (canNavigateBackState.value) canNavigateBackState.value = false
                        if (mapsButtonState.value) mapsButtonState.value = false
                    }

                    BottomNavItem.Search.screenRoute -> {
                        title.value =
                            stringResource(id = R.string.search_screen_title)
                        if (!bottomBarState.value) bottomBarState.value = true
                        if (canNavigateBackState.value) canNavigateBackState.value = false
                        if (refreshButtonState.value) refreshButtonState.value = false
                        mapsButtonState.value = true
                    }

                    BottomNavItem.Settings.screenRoute -> {
                        title.value =
                            stringResource(id = R.string.settings_screen_title)
                        if (!bottomBarState.value) bottomBarState.value = true
                        if (canNavigateBackState.value) canNavigateBackState.value = false
                        if (refreshButtonState.value) refreshButtonState.value = false
                        if (mapsButtonState.value) mapsButtonState.value = false
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        WeatherTopAppBar(
                            title = title.value,
                            modifier = Modifier,
                            navigateUp = { navController.navigateUp() },
                            canNavigateBack = canNavigateBackState.value,
                            refreshButtonState = refreshButtonState.value,
                            onRefreshClicked = {
                                mainViewModel.fetchWeatherData()
                            },
                            mapsButtonState = mapsButtonState.value,
                            onMapsClicked = {
//                                launchDetailsActivity(
//                                    context = this
//                                )
                            }
                        )
                    },
                    bottomBar = {
                        if (bottomBarState.value) {
                            AppBottomNavigationItem(
                                navController = navController,
                                bottomNavigationItems
                            )
                        }
                    },
                    floatingActionButton = {
                        if(mapsButtonState.value) {
                            SearchFloatingActionButton(
                                extended = false,//lazyListState.isScrollingUp(),
                                onClick = {
//                                coroutineScope.launch {
//                                    showEditMessage()
//                                }
                                }
                            )
                        }
                    }
                ) { paddingValues ->
                    Surface(
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        val state = mainViewModel.state.collectAsStateWithLifecycle().value
                        CheckForPermissions(
                            onPermissionGranted =
                            {
                                mainViewModel.processIntent(
                                    MainViewUiState
                                        .GrantPermission(isGranted = true)
                                )
                            },
                            onPermissionDenied = {
                                OnPermissionDenied(activityPermissionResult = permissionRequestLauncher)
                            }
                        )
                        InitMainScreen(state, paddingValues)
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Composable
    private fun InitMainScreen(state: MainViewState, paddingValues: PaddingValues) {
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
                NavigationGraph(navController = navController, mainViewModel, paddingValues)
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
                            latitude = location.latitude
                        )
                    )
                }
            }
        }
    }
}
