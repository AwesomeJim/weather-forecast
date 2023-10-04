package com.awesomejim.weatherforecast.ui.nav

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.awesomejim.weatherforecast.data.model.DefaultLocation
import com.awesomejim.weatherforecast.data.model.LocationItemData
import com.awesomejim.weatherforecast.ui.components.LoadingProgressScreens
import com.awesomejim.weatherforecast.ui.nav.BottomNavItem.LocationPhotos.locationLatTypeArg
import com.awesomejim.weatherforecast.ui.nav.BottomNavItem.LocationPhotos.locationlogTypeArg
import com.awesomejim.weatherforecast.ui.screens.home.ErrorScreen
import com.awesomejim.weatherforecast.ui.screens.home.HomeContentScreen
import com.awesomejim.weatherforecast.ui.screens.main.CurrentWeatherUiState
import com.awesomejim.weatherforecast.ui.screens.main.MainViewModel
import com.awesomejim.weatherforecast.ui.screens.maps.MapsViewModel
import com.awesomejim.weatherforecast.ui.screens.maps.MapsViewScreen
import com.awesomejim.weatherforecast.ui.screens.photos.PhotosScreen
import com.awesomejim.weatherforecast.ui.screens.photos.PhotosViewModel
import com.awesomejim.weatherforecast.ui.screens.search.SearchScreen
import com.awesomejim.weatherforecast.ui.screens.search.SearchViewModel
import com.awesomejim.weatherforecast.ui.screens.settings.SettingsScreen
import com.awesomejim.weatherforecast.ui.screens.settings.SettingsScreenUiState
import com.awesomejim.weatherforecast.ui.screens.settings.SettingsViewModel
import timber.log.Timber

/**
 * BottomNavItem sealed class with bottom navigation item title, item icon and item route
 *
 * @property title - Screen Title
 * @property icon - Screen vector icon
 * @property screenRoute - Navigation route
 * @constructor Create empty Bottom nav item
 */
sealed class BottomNavItem(var title: String, var icon: ImageVector, var screenRoute: String) {

    object Home : BottomNavItem("My Location", Icons.Filled.PinDrop, "home")
    object Search : BottomNavItem("Search", Icons.Filled.Search, "search")
    object Settings : BottomNavItem("Settings", Icons.Filled.Settings, "settings")
    object LocationPhotos :
        BottomNavItem("Location Photos", Icons.Filled.Image, "location_photos") {
        const val locationNameTypeArg = "location_name"
        const val locationLatTypeArg = "latitude"
        const val locationlogTypeArg = "longitude"
        val routeWithArgs =
            "$screenRoute/{$locationNameTypeArg}/{$locationLatTypeArg}/{$locationlogTypeArg}"
        val arguments = listOf(
            navArgument(locationNameTypeArg) { type = NavType.StringType },
            navArgument(locationLatTypeArg) { type = NavType.FloatType },
            navArgument(locationlogTypeArg) { type = NavType.FloatType }
        )
    }

    object MapsView : BottomNavItem("Maps View", Icons.Filled.Settings, "maps_view")
}

/**
 * Navigation graph
 *
 * @param navController
 */

@Composable
fun NavigationGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    paddingValues: PaddingValues,
) {
    NavHost(
        navController, startDestination = BottomNavItem.Home.screenRoute,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(BottomNavItem.Home.screenRoute) {
            val currentWeatherUiState = mainViewModel
                .currentWeatherUiState
                .collectAsStateWithLifecycle().value
            val forecastListState = mainViewModel
                .forecastListState
                .collectAsStateWithLifecycle().value
            mainViewModel.processUiState(currentWeatherUiState)
            when (currentWeatherUiState) {
                is CurrentWeatherUiState.Loading -> {
                    LoadingProgressScreens()
                }

                is CurrentWeatherUiState.Error -> {
                    ErrorScreen(
                        currentWeatherUiState.errorMessageId,
                        onTryAgainClicked = {
                            mainViewModel.fetchWeatherData()
                        }
                    )
                }

                is CurrentWeatherUiState.Success -> {
                    HomeContentScreen(
                        currentWeatherUiState.currentWeather,
                        forecastListState,
                        modifier = Modifier
                    )
                }
            }
        }
        composable(BottomNavItem.Search.screenRoute) {
            val searchViewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(
                searchViewModel = searchViewModel,
                onViewPhotosClick = { locationItemData ->
                    Timber.tag("onViewPhotosClick")
                        .e("locationItemData ${locationItemData.locationName}")
                    navController.navigateToViewPhotos(locationItemData)
                },
                onViewOnMapClick = {
                    navController.navigate(BottomNavItem.MapsView.screenRoute)
                }
            )
        }
        composable(
            BottomNavItem.LocationPhotos.routeWithArgs,
            arguments = BottomNavItem.LocationPhotos.arguments,
            enterTransition = {
                slideInVertically(
                    animationSpec = tween(700),
                    initialOffsetY = { it }
                )
            },
            exitTransition = {
                slideOutVertically(
                    animationSpec = tween(700),
                    targetOffsetY = { it }
                )
            }
        ) { navBackStackEntry ->
            val photosViewModel = hiltViewModel<PhotosViewModel>()
            // Retrieve the passed argument
            val latitude =
                navBackStackEntry.arguments?.getFloat(locationLatTypeArg)
            val longitude =
                navBackStackEntry.arguments?.getFloat(locationlogTypeArg)
            val defaultLocation = DefaultLocation(
                longitude = longitude?.toDouble() ?: 0.0,
                latitude = latitude?.toDouble() ?: 0.0
            )
            PhotosScreen(
                photosViewModel = photosViewModel,
                defaultLocation = defaultLocation
            )
        }
        composable(BottomNavItem.Settings.screenRoute) {
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val settingsState = settingsViewModel
                .state.collectAsStateWithLifecycle()
                .value
            //
            settingsViewModel
                .processSettingsScreenUiState(
                    SettingsScreenUiState
                        .LoadSettingScreenData
                )
            //
            SettingsScreen(
                settingsState,
                onUnitChanged = { selectedUnit ->
                    settingsViewModel.processSettingsScreenUiState(
                        SettingsScreenUiState.ChangeUnits(
                            selectedUnit
                        )
                    )
                }
            )
        }
        composable(BottomNavItem.MapsView.screenRoute,
            enterTransition = {
                slideInVertically(
                    animationSpec = tween(700),
                    initialOffsetY = { it }
                )
            },
            exitTransition = {
                slideOutVertically(
                    animationSpec = tween(700),
                    targetOffsetY = { it }
                )
            }) {
            val mapsViewModel = hiltViewModel<MapsViewModel>()
            MapsViewScreen(
                viewModel = mapsViewModel
            )
        }

    }
}

private fun NavHostController.navigateToViewPhotos(locationItemData: LocationItemData) {
    val locationNameTypeArg = locationItemData.locationName
    val locationLatTypeArg = locationItemData.locationLatitude.toFloat()
    val locationLogTypeArg = locationItemData.locationLongitude.toFloat()
    this.navigate(
        "${
            BottomNavItem
                .LocationPhotos
                .screenRoute
        }/$locationNameTypeArg/$locationLatTypeArg/$locationLogTypeArg"
    )
}

/**
 * App BottomNavigation item
 *
 * @param navController
 */
@Composable
fun AppBottomNavigationItem(
    navController: NavController,
    items: List<BottomNavItem>
) {

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 9.sp
                    )
                },
                alwaysShowLabel = true,
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
