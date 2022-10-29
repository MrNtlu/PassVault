package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.models.OfflinePassword
import com.mrntlu.PassVault.ui.theme.BlueLogo
import com.mrntlu.PassVault.ui.theme.Yellow700
import com.mrntlu.PassVault.ui.widgets.offline.OfflineBottomSheetFields
import com.mrntlu.PassVault.utils.SheetState
import com.mrntlu.PassVault.viewmodels.offline.OfflineBottomSheetViewModel
import com.mrntlu.PassVault.viewmodels.offline.OfflineViewModel

@Composable
fun OfflinePasswordBottomSheet(
    offlineVM: OfflineViewModel,
    sheetState: SheetState<OfflinePassword>,
    isSheetVisible: Boolean,
    onEditClicked: () -> Unit,
    onCancel: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val offlineBottomSheetVM by remember { mutableStateOf(OfflineBottomSheetViewModel()) }

    var idMailError by remember { mutableStateOf(false) }
    var idMailErrorMessage by remember { mutableStateOf("") }

    var passwordError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = sheetState) {
        offlineBottomSheetVM.setStateValues(sheetState)
    }

    LaunchedEffect(key1 = isSheetVisible) {
        if (!isSheetVisible && sheetState is SheetState.AddItem) {
            focusManager.clearFocus(force = true)

            idMailError = false
            passwordError = false

            offlineBottomSheetVM.resetValues()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 48.dp)
                .padding(bottom = 8.dp)
                .padding(top = 8.dp)
                .imePadding()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            OfflineBottomSheetFields(
                offlineBottomSheetVM = offlineBottomSheetVM,
                sheetState = sheetState,
                idMailError, idMailErrorMessage, passwordError, passwordErrorMessage
            )

            val textfieldError = stringResource(R.string.textfield_error)

            BottomSheetButtons(
                confirmBGColor = when (sheetState) {
                    is SheetState.AddItem -> BlueLogo
                    is SheetState.EditItem -> BlueLogo
                    is SheetState.ViewItem -> Yellow700
                },
                confirmText = when (sheetState) {
                    is SheetState.AddItem -> stringResource(R.string.save)
                    is SheetState.EditItem -> stringResource(R.string.update)
                    is SheetState.ViewItem -> stringResource(R.string.edit)
                },
                onConfirmClicked = {
                    focusManager.clearFocus(force = true)

                    if (sheetState is SheetState.ViewItem) {
                        onEditClicked()
                    } else {
                        offlineBottomSheetVM.idMailState.apply {
                            val isIDMailEmpty = isEmpty() || isBlank()
                            idMailError = isIDMailEmpty

                            if (isIDMailEmpty) {
                                idMailErrorMessage = textfieldError
                            }
                        }

                        offlineBottomSheetVM.passwordState.apply {
                            val isPasswordEmpty = isEmpty() || isBlank()
                            passwordError = isPasswordEmpty

                            if (isPasswordEmpty) {
                                passwordErrorMessage = textfieldError
                            }
                        }

                        if (!(idMailError || passwordError)) {
                            when (sheetState) {
                                is SheetState.AddItem -> {
                                    onCancel()

                                    offlineVM.addPassword(
                                        offlineBottomSheetVM.idMailState,
                                        offlineBottomSheetVM.passwordState,
                                        offlineBottomSheetVM.descriptionState,
                                    )
                                }
                                is SheetState.EditItem -> {
                                    onCancel()

                                    offlineVM.editPassword(
                                        sheetState.position,
                                        offlineBottomSheetVM.idMailState,
                                        offlineBottomSheetVM.passwordState,
                                        offlineBottomSheetVM.descriptionState,
                                    )
                                }
                                else -> {}
                            }
                        }
                    }
                },
                dismissText = when (sheetState) {
                    is SheetState.AddItem -> stringResource(id = R.string.cancel)
                    is SheetState.EditItem -> stringResource(id = R.string.cancel)
                    is SheetState.ViewItem -> stringResource(id = R.string.close)
                },
                onDismissClicked = {
                    focusManager.clearFocus(force = true)
                    onCancel()
                }
            )
        }
    }
}

@Preview
@Composable
fun OfflinePasswordBottomSheetPreview() {
    OfflinePasswordBottomSheet(
        viewModel(), SheetState.AddItem, true, {}, {}
    )
}