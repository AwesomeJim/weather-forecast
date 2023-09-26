package com.awesomejim.weatherforecast.ui.nav

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.awesomejim.weatherforecast.ui.components.LoadingProgressScreens
import com.awesomejim.weatherforecast.ui.screens.home.ErrorScreen
import com.awesomejim.weatherforecast.ui.screens.home.HomeContentScreen
import com.awesomejim.weatherforecast.ui.screens.main.CurrentWeatherUiState
import com.awesomejim.weatherforecast.ui.screens.main.MainViewModel
import com.awesomejim.weatherforecast.ui.screens.search.SearchScreen
import com.awesomejim.weatherforecast.ui.screens.search.SearchViewModel
import com.awesomejim.weatherforecast.ui.screens.settings.SettingsScreen
import com.awesomejim.weatherforecast.ui.screens.settings.SettingsScreenUiState
import com.awesomejim.weatherforecast.ui.screens.settings.SettingsViewModel


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
    //object MyLocations : BottomNavItem("My List", Icons.Filled.Favorite, "my_locations")
    object Search : BottomNavItem("Search", Icons.Filled.Search, "search")
    object Settings : BottomNavItem("Settings", Icons.Filled.Settings, "settings")
}


/**
 * Navigation graph
 *
 * @param navController
 */

@Composable
fun NavigationGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    NavHost(navController, startDestination = BottomNavItem.Home.screenRoute) {
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
                        })
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
            SearchScreen(searchViewModel = searchViewModel)

        }
        composable(BottomNavItem.Settings.screenRoute) {
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val settingsState = settingsViewModel
                .state.collectAsStateWithLifecycle()
                .value
            settingsViewModel.processSettingsScreenUiState(SettingsScreenUiState.LoadSettingScreenData)
            SettingsScreen(
                settingsState,
                onUnitChanged = { selectedUnit ->
                    settingsViewModel.processSettingsScreenUiState(
                        SettingsScreenUiState.ChangeUnits(
                            selectedUnit
                        )
                    )
                })
        }
    }
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

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 9.sp
                    )
                },
                selectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                unselectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(0.4f),
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

