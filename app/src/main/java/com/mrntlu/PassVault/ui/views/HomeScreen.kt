package com.mrntlu.PassVault.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import com.mrntlu.PassVault.ui.widgets.online.OnlinePasswordLoading
import com.mrntlu.PassVault.utils.*
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel
import com.mrntlu.PassVault.viewmodels.online.HomeViewModel
import com.mrntlu.PassVault.viewmodels.shared.BillingViewModel
import com.mrntlu.PassVault.viewmodels.shared.MainActivitySharedViewModel
import com.mrntlu.PassVault.viewmodels.shared.OnlinePasswordViewModel
import com.mrntlu.PassVault.viewmodels.shared.ThemeViewModel
import com.parse.ParseUser

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    parseVM: ParseAuthViewModel,
    homeViewModel: HomeViewModel,
    onlineSharedViewModel: OnlinePasswordViewModel,
    billingViewModel: BillingViewModel,
    mainActivitySharedViewModel: MainActivitySharedViewModel,
    themeViewModel: ThemeViewModel,
) {
    val context = LocalContext.current

    val isNetworkAvailable = context.isNetworkConnectionAvailable()
    val isUserLoggedIn by remember { parseVM.isSignedIn }
    val isPurchased by remember { billingViewModel.isPurchased }
    val isDarkThemeEnabled by themeViewModel.isDarkThemeEnabled

    fun interstitialAdsHandler() {
        if (!isPurchased) {
            if (adCount % 3 == 0) {
                showInterstitial(context) {
                    mainActivitySharedViewModel.shouldShowDialog(true)
                }
            }
            adCount++
        }
    }

    LaunchedEffect(key1 = Unit) {
        mainActivitySharedViewModel.fabOnClick.value = {
            interstitialAdsHandler()

            onlineSharedViewModel.changeState(UIState.AddItem)
            navController.navigate("online")
        }
    }

    if (isUserLoggedIn) {
        val passwordsState by homeViewModel.passwords
        var showDialog by remember { mutableStateOf(false) }
        var deleteIndex by remember { mutableStateOf(-1) }

        LaunchedEffect(key1 = Unit) {
            try {
                if (ParseUser.getCurrentUser().username != null && ParseUser.getCurrentUser().email != null) {
                    homeViewModel.getPasswords(isNetworkAvailable)
                }
            } catch (_: Exception) {
                parseVM.parseSignout()
            }
        }

        when(passwordsState) {
            is Response.Loading -> {
                OnlinePasswordLoading(
                    isDarkThemeEnabled = isDarkThemeEnabled
                )
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

                                onlineSharedViewModel.changeState(UIState.EditItem(list[index], index))
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

                                onlineSharedViewModel.changeState(UIState.ViewItem(list[index], index))
                                navController.navigate("online")
                            }
                        }
                    )

                }

                if (showDialog) {
                    CautionDialog(
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

                if (error == "Invalid session token") {
                    parseVM.parseSignout()
                } else {
                    ErrorView(
                        error = error,
                        lottieFile = R.raw.error
                    )
                }
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

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController(), viewModel(), viewModel(), viewModel(), viewModel(), viewModel(), viewModel())
}