package com.mrntlu.PassVault.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mrntlu.PassVault.ui.views.HomeScreen
import com.mrntlu.PassVault.ui.views.OfflineScreen
import com.mrntlu.PassVault.ui.views.SettingsScreen

@Composable
fun NavigationComposable(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home" ) {
        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("offline") {
            OfflineScreen()
        }

        composable("settings") {
            SettingsScreen()
        }
    }
}