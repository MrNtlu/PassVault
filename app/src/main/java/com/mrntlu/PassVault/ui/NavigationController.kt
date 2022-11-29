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
import com.mrntlu.PassVault.viewmodels.shared.OnlinePasswordViewModel
import com.mrntlu.PassVault.viewmodels.shared.ThemeViewModel

@Composable
fun NavigationComposable(
    navController: NavHostController,
    padding: PaddingValues,
    parseVM: ParseAuthViewModel,
    homeVM: HomeViewModel,
    offlineVM: OfflineViewModel,
    onlinePasswordVM: OnlinePasswordViewModel,
    themeViewModel: ThemeViewModel,
    billingViewModel: BillingViewModel,
) {
    NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(padding)) {
        composable("home") {
            HomeScreen(
                navController = navController,
                parseVM = parseVM,
                homeViewModel = homeVM,
                sharedViewModel = onlinePasswordVM,
                billingViewModel = billingViewModel,
            )
        }

        composable("online") {
            OnlinePasswordScreen(
                navController = navController,
                homeViewModel = homeVM,
                sharedViewModel = onlinePasswordVM,
            )
        }

        composable("register") {
            RegisterScreen(
                navController = navController,
                parseVM = parseVM
            )
        }

        composable("offline") {
            OfflineScreen(
                offlineViewModel = offlineVM,
                billingViewModel = billingViewModel,
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