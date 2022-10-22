package com.mrntlu.PassVault.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.mrntlu.PassVault.viewmodels.auth.FirebaseAuthViewModel
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel

fun printLog(tag: String = "Test",message:String) = Log.d(tag,message)

fun navigateByPop(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(route)
        launchSingleTop = true
    }
}

@Composable
fun CheckLoggedIn(
    navController: NavController,
    firebaseVM: FirebaseAuthViewModel,
    parseVM: ParseAuthViewModel
) {
    val alreadyLoggedIn = remember { mutableStateOf(false) }
    val isParseLoggedIn = parseVM.isSignedIn.value
    val isFirebaseLoggedIn = firebaseVM.signedIn.value

    if ((isParseLoggedIn || isFirebaseLoggedIn) && !alreadyLoggedIn.value) {
        alreadyLoggedIn.value = true
        navController.navigate("home") {
            popUpTo(0)
        }
    }
}