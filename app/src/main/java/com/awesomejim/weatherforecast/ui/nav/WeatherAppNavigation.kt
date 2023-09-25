package com.awesomejim.weatherforecast.ui.nav

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.awesomejim.weatherforecast.ui.MainViewModel
import com.awesomejim.weatherforecast.ui.screens.favorite.FavoriteScreen
import com.awesomejim.weatherforecast.ui.screens.home.HomeScreen
import com.awesomejim.weatherforecast.ui.screens.search.SearchScreen
import com.awesomejim.weatherforecast.ui.screens.settings.SettingsScreen


/**
 * BottomNavItem sealed class with bottom navigation item title, item icon and item route
 *
 * @property title - Screen Title
 * @property icon - Screen vector icon
 * @property screen_route - Navigation route
 * @constructor Create empty Bottom nav item
 */
sealed class BottomNavItem(var title: String, var icon: ImageVector, var screen_route: String) {

    object Home : BottomNavItem("My Location", Icons.Filled.PinDrop, "home")
    object MyLocations : BottomNavItem("My List", Icons.Filled.Favorite, "my_locations")
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
    NavHost(navController, startDestination = BottomNavItem.Home.screen_route) {
        composable(BottomNavItem.Home.screen_route) {
            HomeScreen(
                mainViewModel, modifier = Modifier)
        }
        composable(BottomNavItem.MyLocations.screen_route) {
            FavoriteScreen()
        }
        composable(BottomNavItem.Search.screen_route) {
            SearchScreen()
        }
        composable(BottomNavItem.Settings.screen_route) {
            SettingsScreen()
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
        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
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
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.primary.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
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

