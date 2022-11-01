@file:OptIn(ExperimentalMaterialApi::class)

package com.mrntlu.PassVault.ui.views

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
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
import com.mrntlu.PassVault.ui.theme.BlueMidnight
import com.mrntlu.PassVault.ui.widgets.*
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
    homeViewModel: HomeViewModel
) {
    val context = LocalContext.current

    var showInfoDialog by remember { mutableStateOf(false) }
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
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            PasswordBottomSheet(
                homeVM = homeViewModel,
                sheetState = sheetState,
                isSheetVisible = modalSheetState.isVisible,
                onEditClicked = {
                    sheetState =
                        SheetState.EditItem(sheetState.getItem()!!, sheetState.getPosition()!!)
                },
                onInfoDialogClicked = { showInfoDialog = true },
                onCancel = {
                    coroutineScope.launch { modalSheetState.hide() }
                }
            )
        },
    ) {
        Scaffold(
            modifier = Modifier.setGradientBackground(),
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
                            Icon(
                                modifier = Modifier.size(28.dp),
                                imageVector = Icons.Rounded.Add,
                                contentDescription = stringResource(id = R.string.add)
                            )
                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            isFloatingActionButtonDocked = false,
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
                                BannerAdView()

                                val passwords = (homeViewModel.passwords.value as Response.Success).data

                                passwords?.let { list ->
                                    if (list.isNotEmpty()) {
                                        Text(
                                            modifier = Modifier
                                                .padding(horizontal = 4.dp)
                                                .padding(top = 3.dp),
                                            text = stringResource(id = R.string.list_item_info),
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                        )
                                    }

                                    OnlinePasswordList(
                                        passwords = list,
                                        onEditClicked = { index ->
                                            sheetState = SheetState.EditItem(list[index].toPasswordItem(), index)

                                            coroutineScope.launch {
                                                if (modalSheetState.isVisible)
                                                    modalSheetState.hide()
                                                else
                                                    modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                            }
                                        },
                                        onDeleteClicked = { index ->
                                            showDialog = true
                                            deleteIndex = index
                                        },
                                        onItemClicked = { index ->
                                            if (adCount % 4 == 1) {
                                                loadInterstitial(context)
                                                showInterstitial(context)
                                            }
                                            adCount++

                                            sheetState = SheetState.ViewItem(list[index].toPasswordItem(), index)
                                            coroutineScope.launch {
                                                if (modalSheetState.isVisible)
                                                    modalSheetState.hide()
                                                else
                                                    modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                            }
                                        }
                                    )
                                }
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

                        else -> {}
                    }
                } else {
                    LoginScreen(
                        navController = navController,
                        firebaseVM = firebaseVM,
                        parseVM = parseVM,
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController(), viewModel(), viewModel(), viewModel())
}