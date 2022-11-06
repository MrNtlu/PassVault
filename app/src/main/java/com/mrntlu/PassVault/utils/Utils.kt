package com.mrntlu.PassVault.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel

fun printLog(message:String, tag: String = "Test") = Log.d(tag,message)

fun navigateByPop(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(route)
        launchSingleTop = true
    }
}

@Composable
fun CheckLoggedIn(
    navController: NavController,
    parseVM: ParseAuthViewModel
) {
    val alreadyLoggedIn = remember { mutableStateOf(false) }
    val isParseLoggedIn = parseVM.isSignedIn.value

    if (isParseLoggedIn && !alreadyLoggedIn.value) {
        alreadyLoggedIn.value = true
        navController.navigate("home") {
            popUpTo(0)
        }
    }
}