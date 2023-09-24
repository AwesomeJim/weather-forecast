package com.awesomejim.weatherforecast.ui.screens

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.awesomejim.weatherforecast.ui.nav.AppBottomNavigationItem
import com.awesomejim.weatherforecast.ui.nav.BottomNavItem
import com.awesomejim.weatherforecast.ui.nav.NavigationGraph

@Composable
fun MainScreenView() {
    val navController = rememberNavController()

    val bottomNavigationItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.MyLocations,
        BottomNavItem.Search,
        BottomNavItem.Settings
    )
    Scaffold(
        bottomBar = {
            AppBottomNavigationItem(navController = navController, bottomNavigationItems)
        },
    ) {

        NavigationGraph(navController = navController)
    }
}