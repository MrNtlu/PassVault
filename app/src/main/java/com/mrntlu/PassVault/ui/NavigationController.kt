package com.mrntlu.PassVault.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mrntlu.PassVault.ui.views.HomeScreen
import com.mrntlu.PassVault.ui.views.OfflineScreen
import com.mrntlu.PassVault.ui.views.SettingsScreen

@Composable
fun NavigationComposable(navController: NavHostController, padding: PaddingValues) {
    NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(padding) ) {
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