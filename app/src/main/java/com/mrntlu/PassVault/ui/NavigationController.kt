package com.mrntlu.PassVault.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mrntlu.PassVault.ui.views.*
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel
import com.mrntlu.PassVault.viewmodels.offline.OfflineViewModel
import com.mrntlu.PassVault.viewmodels.online.HomeViewModel
import com.mrntlu.PassVault.viewmodels.shared.BillingViewModel
import com.mrntlu.PassVault.viewmodels.shared.MainActivitySharedViewModel
import com.mrntlu.PassVault.viewmodels.shared.OnlinePasswordViewModel
import com.mrntlu.PassVault.viewmodels.shared.ThemeViewModel

@Composable
fun NavigationComposable(
    navController: NavHostController,
    padding: PaddingValues,
    parseViewModel: ParseAuthViewModel,
    homeViewModel: HomeViewModel,
    offlineViewModel: OfflineViewModel,
    onlinePasswordViewModel: OnlinePasswordViewModel,
    themeViewModel: ThemeViewModel,
    billingViewModel: BillingViewModel,
    sharedViewModel: MainActivitySharedViewModel,
) {
    NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(padding)) {
        composable("home") {
            HomeScreen(
                navController = navController,
                parseVM = parseViewModel,
                homeViewModel = homeViewModel,
                onlineSharedViewModel = onlinePasswordViewModel,
                billingViewModel = billingViewModel,
                mainActivitySharedViewModel = sharedViewModel,
            )
        }

        composable("online") {
            OnlinePasswordScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                onlineSharedViewModel = onlinePasswordViewModel,
            )
        }

        composable("register") {
            RegisterScreen(
                navController = navController,
                parseVM = parseViewModel
            )
        }

        composable("offline") {
            OfflineScreen(
                offlineViewModel = offlineViewModel,
                billingViewModel = billingViewModel,
                sharedViewModel = sharedViewModel,
            )
        }

        composable("settings") {
            SettingsScreen(
                themeViewModel = themeViewModel,
                billingViewModel = billingViewModel,
                navController = navController,
            )
        }

        composable(
            route = "policy/{isTerm}",
            arguments = listOf(
                navArgument("isTerm") {
                    NavType.BoolType
                }
            )
        ) {
            PolicyScreen(it.arguments?.getString("isTerm")?.toBoolean() ?: false)
        }
    }
}