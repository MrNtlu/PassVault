@file:OptIn(ExperimentalMaterialApi::class)

package com.mrntlu.PassVault.ui.views

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.repositories.HomeRepository
import com.mrntlu.PassVault.ui.theme.BlueDark
import com.mrntlu.PassVault.ui.theme.BlueDarkest
import com.mrntlu.PassVault.ui.theme.BlueLogo
import com.mrntlu.PassVault.ui.theme.BlueMidnight
import com.mrntlu.PassVault.ui.widgets.LoadingView
import com.mrntlu.PassVault.ui.widgets.OnlinePasswordListItem
import com.mrntlu.PassVault.ui.widgets.PasswordBottomSheet
import com.mrntlu.PassVault.utils.Response
import com.mrntlu.PassVault.viewmodels.HomeViewModel
import com.mrntlu.PassVault.viewmodels.auth.FirebaseAuthViewModel
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    firebaseVM: FirebaseAuthViewModel,
    parseVM: ParseAuthViewModel,
) {
    val homeRepository = HomeRepository()
    val homeViewModel by remember { mutableStateOf(HomeViewModel(homeRepository = homeRepository)) }

    val isParseLoggedIn by remember { mutableStateOf(parseVM.isSignedIn) }

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.Expanded }
    )
    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            PasswordBottomSheet()
        },
    ) {
        Scaffold(
            floatingActionButton = {
                if (isParseLoggedIn.value) {
                    FloatingActionButton(
                        onClick = {
                            coroutineScope.launch {
                                if (sheetState.isVisible) sheetState.hide()
                                else sheetState.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        },
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
                if (isParseLoggedIn.value) {
                    val uiState by homeViewModel.passwords

                    LaunchedEffect(key1 = true) {
                        homeViewModel.getPasswords()
                    }

                    when(uiState) {
                        is Response.Loading -> {
                            LoadingView()
                        }

                        is Response.Success<List<PasswordItem>> -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                BlueLogo,
                                                BlueDark,
                                                BlueDarkest
                                            )
                                        )
                                    ),
                            ) {
                                val passwords = (homeViewModel.passwords.value as Response.Success).data

                                passwords?.let {
                                    LazyColumn {
                                        items(
                                            count = it.size
                                        ) { index ->
                                            val password = passwords[index]

                                            OnlinePasswordListItem(password = password)
                                        }
                                    }
                                }
                            }
                        }

                        else -> {}
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "You need to login!")

                        Button(onClick = { navController.navigate("login") }) {
                            Text(text = "Login")
                        }
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController(), viewModel(), viewModel())
}