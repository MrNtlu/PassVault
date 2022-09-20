package com.mrntlu.PassVault.ui.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mrntlu.PassVault.ui.theme.BlueLogo
import com.mrntlu.PassVault.viewmodels.AuthViewModel
import com.mrntlu.PassVault.viewmodels.HomeScreenState
import com.mrntlu.PassVault.viewmodels.HomeScreenViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    val homeScreenViewModel by remember { mutableStateOf(HomeScreenViewModel()) }
    val authViewModel by remember { mutableStateOf(AuthViewModel(homeScreenViewModel = homeScreenViewModel)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = {}){
                        Icon(imageVector = Icons.Rounded.ExitToApp, contentDescription = "Log out", tint = Color.White)
                    }
                },
                elevation = 8.dp,
                backgroundColor = BlueLogo
            )
        },
        content = {
            Log.d("Test", homeScreenViewModel.state.value.toString())
            when(homeScreenViewModel.state.value) {
                HomeScreenState.Login -> {
                    LoginScreen(navController = navController, authViewModel = authViewModel)
                }
                HomeScreenState.Register -> {
                    RegisterScreen(navController = navController, authViewModel = authViewModel, homeScreenViewModel = homeScreenViewModel)
                }
                else -> {
                    Text("Yay")
                }
            }
        }
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}