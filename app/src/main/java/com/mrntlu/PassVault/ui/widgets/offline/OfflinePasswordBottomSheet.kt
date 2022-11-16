package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.mrntlu.PassVault.utils.UIState
import com.mrntlu.PassVault.viewmodels.offline.OfflineBottomSheetViewModel
import com.mrntlu.PassVault.viewmodels.offline.OfflineViewModel

@Composable
fun OfflinePasswordBottomSheet(
    offlineVM: OfflineViewModel,
    uiState: UIState<OfflinePassword>,
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

    LaunchedEffect(key1 = uiState) {
        offlineBottomSheetVM.setStateValues(uiState)
    }

    LaunchedEffect(key1 = isSheetVisible) {
        if (!isSheetVisible && uiState is UIState.AddItem) {
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
                .verticalScroll(rememberScrollState())
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
                uiState = uiState,
                idMailError, idMailErrorMessage, passwordError, passwordErrorMessage
            )

            val textfieldError = stringResource(R.string.textfield_error)

            BottomSheetButtons(
                confirmBGColor = when (uiState) {
                    is UIState.AddItem -> BlueLogo
                    is UIState.EditItem -> BlueLogo
                    is UIState.ViewItem -> Yellow700
                },
                confirmText = when (uiState) {
                    is UIState.AddItem -> stringResource(R.string.save)
                    is UIState.EditItem -> stringResource(R.string.update)
                    is UIState.ViewItem -> stringResource(R.string.edit)
                },
                onConfirmClicked = {
                    focusManager.clearFocus(force = true)

                    if (uiState is UIState.ViewItem) {
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
                            when (uiState) {
                                is UIState.AddItem -> {
                                    onCancel()

                                    offlineVM.addPassword(
                                        offlineBottomSheetVM.idMailState,
                                        offlineBottomSheetVM.passwordState,
                                        offlineBottomSheetVM.descriptionState,
                                    )
                                }
                                is UIState.EditItem -> {
                                    onCancel()

                                    offlineVM.editPassword(
                                        uiState.position,
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
                dismissText = when (uiState) {
                    is UIState.AddItem -> stringResource(id = R.string.cancel)
                    is UIState.EditItem -> stringResource(id = R.string.cancel)
                    is UIState.ViewItem -> stringResource(id = R.string.close)
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
        viewModel(), UIState.AddItem, true, {}, {}
    )
}