package com.awesomejim.weatherforecast.ui.screens.main

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.awesomejim.weatherforecast.ui.nav.AppBottomNavigationItem
import com.awesomejim.weatherforecast.ui.nav.BottomNavItem
import com.awesomejim.weatherforecast.ui.nav.NavigationGraph

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreenView(mainViewModel: MainViewModel) {
    val navController = rememberNavController()

    val bottomNavigationItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.myPhotos,
        BottomNavItem.Settings
    )
    Scaffold(
        bottomBar = {
            AppBottomNavigationItem(navController = navController, bottomNavigationItems)
        },
    ) {

        NavigationGraph(navController = navController, mainViewModel)
    }
}