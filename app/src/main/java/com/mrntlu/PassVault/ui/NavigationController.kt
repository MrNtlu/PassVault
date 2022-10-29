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
import com.mrntlu.PassVault.viewmodels.HomeViewModel
import com.mrntlu.PassVault.viewmodels.auth.FirebaseAuthViewModel
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel
import com.mrntlu.PassVault.viewmodels.offline.OfflineViewModel

@Composable
fun NavigationComposable(
    navController: NavHostController,
    padding: PaddingValues,
    firebaseVM: FirebaseAuthViewModel,
    parseVM: ParseAuthViewModel,
    homeVM: HomeViewModel,
    offlineVM: OfflineViewModel,
) {
    NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(padding)) {
        composable("home") {
            HomeScreen(
                navController = navController,
                parseVM = parseVM,
                firebaseVM = firebaseVM,
                homeViewModel = homeVM,
            )
        }

        composable("register") {
            RegisterScreen(
                navController = navController,
                firebaseVM = firebaseVM,
                parseVM = parseVM
            )
        }

        composable("login") {
            LoginScreen(
                navController = navController,
                firebaseVM = firebaseVM,
                parseVM = parseVM
            )
        }

        composable("offline") {
            OfflineScreen(
                offlineViewModel = offlineVM
            )
        }

        composable("settings") {
            SettingsScreen(
                navController = navController
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