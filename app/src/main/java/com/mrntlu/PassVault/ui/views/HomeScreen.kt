package com.mrntlu.PassVault.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.ui.theme.Red500
import com.mrntlu.PassVault.ui.widgets.*
import com.mrntlu.PassVault.utils.*
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel
import com.mrntlu.PassVault.viewmodels.online.HomeViewModel
import com.mrntlu.PassVault.viewmodels.shared.OnlinePasswordViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    parseVM: ParseAuthViewModel,
    homeViewModel: HomeViewModel,
    sharedViewModel: OnlinePasswordViewModel,
) {
    val context = LocalContext.current

    var showInfoDialog by remember { mutableStateOf(false) }
    var isNetworkAvailable by remember { mutableStateOf(true) }
    val isParseLoggedIn by remember { mutableStateOf(parseVM.isSignedIn) }

    //TODO: Add Category Chip
    Scaffold(
        floatingActionButton = {
            if (isParseLoggedIn.value && isNetworkAvailable) {
                val passwords by homeViewModel.passwords

                if (passwords is Response.Success) {
                    FloatingActionButton(
                        onClick = {
                            if (adCount % 4 == 1) {
                                loadInterstitial(context)
                                showInterstitial(context)
                            }
                            adCount++

                            sharedViewModel.changeState(UIState.AddItem)
                            navController.navigate("online")
                        },
                        backgroundColor = MaterialTheme.colorScheme.onBackground,
                        contentColor = MaterialTheme.colorScheme.background,
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Rounded.Add,
                            contentDescription = stringResource(id = R.string.add),
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = false,
        content = {
            isNetworkAvailable = context.isNetworkConnectionAvailable()

            if (isParseLoggedIn.value) {
                val passwordsState by homeViewModel.passwords
                var showDialog by remember { mutableStateOf(false) }
                var deleteIndex by remember { mutableStateOf(-1) }

                LaunchedEffect(key1 = true) {
                    homeViewModel.getPasswords(context.isNetworkConnectionAvailable())
                }

                when(passwordsState) {
                    is Response.Loading -> {
                        LoadingView()
                    }

                    is Response.Success<List<PasswordItem>> -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                        ) {
                            if (isNetworkAvailable) {
                                BannerAdView()
                            } else {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Red500)
                                        .padding(horizontal = 3.dp)
                                        .padding(vertical = 1.dp),
                                    text = stringResource(R.string.no_internet),
                                    color = Color.White,
                                    fontSize = 13.sp,
                                )
                            }

                            val passwords = (passwordsState as Response.Success).data

                            OnlinePasswordList(
                                passwords = passwords,
                                onEditClicked = { index ->
                                    passwords?.let { list ->
                                        sharedViewModel.changeState(UIState.EditItem(list[index], index))
                                        navController.navigate("online")
                                    }
                                },
                                onDeleteClicked = { index ->
                                    showDialog = true
                                    deleteIndex = index
                                },
                                onItemClicked = { index ->
                                    passwords?.let { list ->
                                        if (adCount % 4 == 1) {
                                            loadInterstitial(context)
                                            showInterstitial(context)
                                        }
                                        adCount++

                                        sharedViewModel.changeState(UIState.ViewItem(list[index], index))
                                        navController.navigate("online")
                                    }
                                }
                            )

                        }

                        if (showDialog) {
                            AYSDialog(
                                text = stringResource(R.string.ays_delete),
                                onConfirmClicked = {
                                    showDialog = false
                                    homeViewModel.deletePassword(deleteIndex)
                                    deleteIndex = -1
                                }
                            ) {
                                showDialog = false
                            }
                        }

                        if (showInfoDialog) {
                            AlertDialog(
                                onDismissRequest = { showInfoDialog = false },
                                title = {
                                    Text(
                                        text = stringResource(R.string.cd_what_encryption),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                },
                                text = {
                                    Text(
                                        text = stringResource(id = R.string.encryption_explanation),
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                },
                                confirmButton = {},
                                dismissButton = {
                                    Button(
                                        onClick = { showInfoDialog = false },
                                    ) {
                                        Text(stringResource(R.string.ok))
                                    }
                                }
                            )
                        }
                    }

                    is Response.Failure -> {
                        val error = (passwordsState as Response.Failure).errorMessage

                        ErrorView(
                            error = error,
                            lottieFile = R.raw.error
                        )
                    }

                    else -> {}
                }
            } else {
                if (isNetworkAvailable) {
                    LoginScreen(
                        navController = navController,
                        parseVM = parseVM,
                    )
                } else {
                    ErrorView(
                        error = "No Internet Connection",
                        lottieFile = R.raw.no_internet,
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController(), viewModel(), viewModel(), viewModel())
}