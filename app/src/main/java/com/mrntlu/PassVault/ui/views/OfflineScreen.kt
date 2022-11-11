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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.models.OfflinePassword
import com.mrntlu.PassVault.ui.theme.BlueMidnight
import com.mrntlu.PassVault.ui.widgets.AYSDialog
import com.mrntlu.PassVault.ui.widgets.BannerAdView
import com.mrntlu.PassVault.ui.widgets.OfflinePasswordBottomSheet
import com.mrntlu.PassVault.ui.widgets.OfflinePasswordList
import com.mrntlu.PassVault.utils.*
import com.mrntlu.PassVault.viewmodels.offline.OfflineViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OfflineScreen(
    offlineViewModel: OfflineViewModel,
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        offlineViewModel.getOfflinePasswords()
    }

    var showDialog by remember { mutableStateOf(false) }
    var deleteIndex by remember { mutableStateOf(-1) }

    var sheetState by remember { mutableStateOf<SheetState<OfflinePassword>>(SheetState.AddItem) }

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { false },
        skipHalfExpanded = true
    )

    BackHandler(modalSheetState.isVisible) {
        coroutineScope.launch { modalSheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            OfflinePasswordBottomSheet(
                offlineVM = offlineViewModel,
                sheetState = sheetState,
                isSheetVisible = modalSheetState.isVisible,
                onEditClicked = {
                    sheetState = SheetState.EditItem(sheetState.getItem()!!, sheetState.getPosition()!!)
                },
                onCancel = {
                    coroutineScope.launch { modalSheetState.hide() }
                }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .setGradientBackground(),
            floatingActionButton = {
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
                        contentDescription = stringResource(R.string.cd_add)
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            isFloatingActionButtonDocked = false,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .setGradientBackground()
                ) {
                    BannerAdView()

                    val passwords by offlineViewModel.password

                    passwords?.let { list ->
                        OfflinePasswordList(
                            passwords = list,
                            onEditClicked = { index ->
                                sheetState = SheetState.EditItem(list[index], index)

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
                            onDescriptionClicked = { index ->
                                sheetState = SheetState.ViewItem(list[index], index)

                                coroutineScope.launch {
                                    if (modalSheetState.isVisible)
                                        modalSheetState.hide()
                                    else
                                        modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                }
                            }
                        )
                    }

                    if (showDialog) {
                        AYSDialog(
                            text = stringResource(id = R.string.ays_delete),
                            onConfirmClicked = {
                                showDialog = false
                                offlineViewModel.deletePassword(deleteIndex)
                                deleteIndex = -1
                            }
                        ) {
                            showDialog = false
                        }
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun OfflineScreenPreview() {
    OfflineScreen(viewModel())
}