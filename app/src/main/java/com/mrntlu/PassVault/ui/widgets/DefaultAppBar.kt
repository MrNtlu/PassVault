package com.mrntlu.PassVault.ui.widgets

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mrntlu.PassVault.ui.theme.BlueLogo

@Composable
fun DefaultAppBar(
    navController: NavController,
    isUserLoggedIn: Boolean,
    isAuthLoading: Boolean,
    showBottomBar: Boolean,
    isCurrentScreenHome: Boolean,
    onSearchClicked: () -> Unit,
    onLogOutClicked: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = when(navController.currentDestination?.route) {
                    "login" -> {
                        "Login"
                    }
                    "register" -> {
                        "Register"
                    }
                    else -> ""
                },
                color = Color.White
            )
        },
        navigationIcon = {
            if (!showBottomBar && navController.previousBackStackEntry != null && !isAuthLoading) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }

            if (isCurrentScreenHome && isUserLoggedIn) {
                IconButton(onClick = onSearchClicked) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            }
        },
        actions = {
            if (isCurrentScreenHome && isUserLoggedIn) {
                IconButton(
                    onClick = onLogOutClicked
                ){
                    Icon(imageVector = Icons.Rounded.Logout, contentDescription = "Log out", tint = Color.White)
                }
            }
        },
        elevation = 8.dp,
        backgroundColor = BlueLogo
    )
}

@Preview
@Composable
fun DefaultAppBarPreview() {
    DefaultAppBar(navController = rememberNavController(), isUserLoggedIn = true, isAuthLoading = false, showBottomBar = true, isCurrentScreenHome = true, onLogOutClicked = {}, onSearchClicked = {})
}