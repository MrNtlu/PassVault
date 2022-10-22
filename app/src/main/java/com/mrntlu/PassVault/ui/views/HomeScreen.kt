@file:OptIn(ExperimentalMaterialApi::class)

package com.mrntlu.PassVault.ui.views

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.repositories.HomeRepository
import com.mrntlu.PassVault.ui.theme.BlueMidnight
import com.mrntlu.PassVault.ui.widgets.AYSDialog
import com.mrntlu.PassVault.ui.widgets.LoadingView
import com.mrntlu.PassVault.ui.widgets.OnlinePasswordListItem
import com.mrntlu.PassVault.ui.widgets.PasswordBottomSheet
import com.mrntlu.PassVault.utils.*
import com.mrntlu.PassVault.viewmodels.HomeViewModel
import com.mrntlu.PassVault.viewmodels.auth.FirebaseAuthViewModel
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel
import com.parse.ParseObject
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    firebaseVM: FirebaseAuthViewModel,
    parseVM: ParseAuthViewModel,
) {
    val context = LocalContext.current
    val homeRepository = HomeRepository()

    val homeViewModel by remember { mutableStateOf(HomeViewModel(homeRepository = homeRepository)) }
    val isParseLoggedIn by remember { mutableStateOf(parseVM.isSignedIn) }
    var sheetState by remember { mutableStateOf<SheetState<PasswordItem>>(SheetState.AddItem) }

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = {
            it != ModalBottomSheetValue.Expanded
        },
        skipHalfExpanded = true
    )

    BackHandler(modalSheetState.isVisible) {
        coroutineScope.launch { modalSheetState.hide() }
    }

    LaunchedEffect(key1 = isParseLoggedIn.value) {
        if (modalSheetState.isVisible)
            modalSheetState.animateTo(ModalBottomSheetValue.Hidden)
    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetContent = {
            PasswordBottomSheet(homeVM = homeViewModel, sheetState = sheetState, onEditClicked = {
                sheetState = SheetState.EditItem(sheetState.getItem()!!)
            }) {
                coroutineScope.launch { modalSheetState.hide() }
            }
        },
    ) {
        Scaffold(
            floatingActionButton = {
                if (isParseLoggedIn.value) {
                    val uiState by homeViewModel.passwords

                    if (uiState is Response.Success) {
                        FloatingActionButton(
                            onClick = {
                                if (adCount % 4 == 1) {
                                    loadInterstitial(context)
                                    showInterstitial(context)
                                }
                                adCount++

                                coroutineScope.launch {
                                    sheetState = SheetState.AddItem

                                    if (modalSheetState.isVisible)
                                        modalSheetState.hide()
                                    else
                                        modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                }
                            },
                            backgroundColor = BlueMidnight,
                            contentColor = Color.White,
                        ) {
                            Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            isFloatingActionButtonDocked = false,
            modifier = Modifier.setGradientBackground(),
            content = {
                if (isParseLoggedIn.value) {
                    val uiState by homeViewModel.passwords
                    var showDialog by remember { mutableStateOf(false) }
                    var deleteIndex by remember { mutableStateOf(-1) }

                    LaunchedEffect(key1 = true) {
                        homeViewModel.getPasswords()
                    }

                    when(uiState) {
                        is Response.Loading -> {
                            LoadingView()
                        }

                        is Response.Success<List<ParseObject>> -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .setGradientBackground(),
                            ) {
                                AndroidView(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    factory = { context ->
                                        AdView(context).apply {
                                            setAdSize(AdSize.BANNER)
                                            adUnitId = context.getString(R.string.banner_ad_unit_id)
                                            loadAd(AdRequest.Builder().build())
                                        }
                                    }
                                )

                                val passwords = (homeViewModel.passwords.value as Response.Success).data

                                passwords?.let {

                                    //TODO Move to separate widget
                                    LazyColumn {
                                        items(
                                            count = it.size
                                        ) { index ->
                                            val password = passwords[index]

                                            OnlinePasswordListItem(
                                                index = index,
                                                onEditClicked = {
                                                    sheetState = SheetState.EditItem(passwords[it].toPasswordItem())

                                                    coroutineScope.launch {
                                                        if (modalSheetState.isVisible)
                                                            modalSheetState.hide()
                                                        else
                                                            modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                                    }
                                                },
                                                onDeleteClicked = {
                                                    showDialog = true
                                                    deleteIndex = it
                                                },
                                                onItemClicked = {
                                                    if (adCount % 4 == 1) {
                                                        loadInterstitial(context)
                                                        showInterstitial(context)
                                                    }
                                                    adCount++

                                                    sheetState = SheetState.ViewItem(passwords[it].toPasswordItem())
                                                    coroutineScope.launch {
                                                        if (modalSheetState.isVisible)
                                                            modalSheetState.hide()
                                                        else
                                                            modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                                    }
                                                },
                                                password = password.toPasswordItem()
                                            )
                                        }
                                    }
                                }
                            }

                            if (showDialog) {
                                AYSDialog(
                                    text = "Do you want to delete?",
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