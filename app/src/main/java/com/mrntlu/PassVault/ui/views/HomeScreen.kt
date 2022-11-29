package com.mrntlu.PassVault.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.ui.widgets.*
import com.mrntlu.PassVault.utils.*
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel
import com.mrntlu.PassVault.viewmodels.online.HomeViewModel
import com.mrntlu.PassVault.viewmodels.shared.BillingViewModel
import com.mrntlu.PassVault.viewmodels.shared.OnlinePasswordViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    parseVM: ParseAuthViewModel,
    homeViewModel: HomeViewModel,
    sharedViewModel: OnlinePasswordViewModel,
    billingViewModel: BillingViewModel,
) {
    val context = LocalContext.current

    var isNetworkAvailable by remember { mutableStateOf(true) }
    val isParseLoggedIn by remember { mutableStateOf(parseVM.isSignedIn) }
    val isPurchased by remember { billingViewModel.isPurchased }

    fun interstitialAdsHandler() {
        if (!isPurchased) {
            if (adCount % 4 == 1) {
                loadInterstitial(context)
                showInterstitial(context)
            }
            adCount++
        }
    }

    //TODO: Add Category Chip
    Scaffold(
        floatingActionButton = {
            if (isParseLoggedIn.value && isNetworkAvailable) {
                val passwords by homeViewModel.passwords

                if (passwords is Response.Success) {
                    FloatingActionButton(
                        onClick = {
                            interstitialAdsHandler()

                            sharedViewModel.changeState(UIState.AddItem)
                            navController.navigate("online")
                        },
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Rounded.Add,
                            contentDescription = stringResource(id = R.string.add),
                            tint = MaterialTheme.colorScheme.background,
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
                    homeViewModel.getPasswords(isNetworkAvailable)
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
                            if (isNetworkAvailable && !isPurchased) {
                                BannerAdView()
                            } else if (!isNetworkAvailable) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.error)
                                        .padding(horizontal = 3.dp)
                                        .padding(vertical = 1.dp),
                                    text = stringResource(R.string.no_internet),
                                    color = MaterialTheme.colorScheme.onError,
                                    fontSize = 13.sp,
                                )
                            }

                            val passwords = (passwordsState as Response.Success).data

                            OnlinePasswordList(
                                passwords = passwords,
                                onEditClicked = { index ->
                                    passwords?.let { list ->
                                        interstitialAdsHandler()

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
                                        interstitialAdsHandler()

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
    HomeScreen(rememberNavController(), viewModel(), viewModel(), viewModel(), viewModel())
}