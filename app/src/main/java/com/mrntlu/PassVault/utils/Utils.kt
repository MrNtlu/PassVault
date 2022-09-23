package com.mrntlu.PassVault.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.viewmodels.auth.FirebaseAuthViewModel
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel
import com.parse.ParseObject

fun navigateByPop(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(route)
        launchSingleTop = true
    }
}

fun parseObjectToPasswordItem(parseObject: ParseObject) = PasswordItem(
    parseObject.getString("Username") ?: "",
    parseObject.getString("Title") ?: "",
    parseObject.getString("Note"),
    parseObject.getString("Password") ?: "",
)

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