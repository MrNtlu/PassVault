package com.mrntlu.PassVault.ui.views

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mrntlu.PassVault.repositories.HomeRepository
import com.mrntlu.PassVault.ui.theme.BlueLogo
import com.mrntlu.PassVault.ui.theme.BlueMidnight
import com.mrntlu.PassVault.ui.widgets.ErrorDialog
import com.mrntlu.PassVault.ui.widgets.LoadingView
import com.mrntlu.PassVault.utils.Response
import com.mrntlu.PassVault.viewmodels.AuthViewModel
import com.mrntlu.PassVault.viewmodels.HomeScreenState
import com.mrntlu.PassVault.viewmodels.HomeStateViewModel
import com.mrntlu.PassVault.viewmodels.HomeViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    val homeRepository = HomeRepository()

    val homeStateViewModel by remember { mutableStateOf(HomeStateViewModel()) }
    val authViewModel by remember { mutableStateOf(AuthViewModel(hsViewModel = homeStateViewModel)) }
    val homeViewModel by remember { mutableStateOf(HomeViewModel(hsViewModel = homeStateViewModel, homeRepository = homeRepository)) }
    val authUIState by homeStateViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    if (homeStateViewModel.state.value == HomeScreenState.LoggedIn) {
                        IconButton(onClick = {
                            authViewModel.parseSignout()
                        }){
                            Icon(imageVector = Icons.Rounded.Logout, contentDescription = "Log out", tint = Color.White)
                        }
                    }
                },
                elevation = 8.dp,
                backgroundColor = BlueLogo
            )
        },
        floatingActionButton = {
           if (
               homeStateViewModel.state.value == HomeScreenState.LoggedIn &&
               authUIState !is Response.Loading
           ) {
               FloatingActionButton(
                   onClick = {},
                   backgroundColor = BlueMidnight,
                   contentColor = Color.White,
               ) {
                   Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
               }
           }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = false,
        content = {
            when(homeStateViewModel.state.value) {
                HomeScreenState.Login, HomeScreenState.Register -> {

                    if (homeStateViewModel.state.value == HomeScreenState.Login) {
                        LoginScreen(
                            onLoginClicked = { username, password ->
                                authViewModel.parseLogin(username, password)
                            },
                            onRegisterClicked = {
                                homeStateViewModel.state.value = HomeScreenState.Register
                            }
                        )
                    } else {
                        RegisterScreen(
                            onRegisterClicked = { userRegister ->
                                authViewModel.parseRegister(userRegister)
                            },
                            onLoginClicked = {
                                homeStateViewModel.state.value = HomeScreenState.Login
                            }
                        )
                    }

                    if(authUIState is Response.Failure) {
                        val showDialog = remember { mutableStateOf(true)  }

                        if (showDialog.value) {
                            val state = (authUIState as Response.Failure)

                            ErrorDialog(error = state.e?.message ?: state.e.toString()) {
                                showDialog.value = false
                            }
                        }
                    }

                    if (authUIState is Response.Loading) {
                        LoadingView()
                    }
                }

                HomeScreenState.LoggedIn -> {
                    val uiState by homeViewModel.passwords

                    LaunchedEffect(key1 = true) {
                        homeViewModel.getPasswords()
                    }

                    when(uiState) {
                        is Response.Loading -> {
                            LoadingView()
                        }

                        else -> {
                            OnlineStorageScreen(homeViewModel = homeViewModel)

                            if(authUIState is Response.Failure) {
                                val showDialog = remember { mutableStateOf(true)  }

                                if (showDialog.value) {
                                    val state = (authUIState as Response.Failure)

                                    ErrorDialog(error = state.e?.message ?: state.e.toString()) {
                                        showDialog.value = false
                                    }
                                }
                            }
                        }
                    }
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