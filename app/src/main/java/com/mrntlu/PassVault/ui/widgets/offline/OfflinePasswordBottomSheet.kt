package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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

    LaunchedEffect(key1 = uiState) {
        offlineBottomSheetVM.setStateValues(uiState)
    }

    LaunchedEffect(key1 = isSheetVisible) {
        if (!isSheetVisible && uiState is UIState.AddItem) {
            focusManager.clearFocus(force = true)
            offlineBottomSheetVM.resetValues()
        }
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
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
            )

            val textfieldError = stringResource(R.string.textfield_error)

            BottomSheetButtons(
                confirmBGColor = when (uiState) {
                    is UIState.AddItem -> MaterialTheme.colorScheme.primary
                    is UIState.EditItem -> MaterialTheme.colorScheme.primary
                    is UIState.ViewItem -> MaterialTheme.colorScheme.primaryContainer
                },
                confirmText = when (uiState) {
                    is UIState.AddItem -> stringResource(R.string.save)
                    is UIState.EditItem -> stringResource(R.string.update)
                    is UIState.ViewItem -> stringResource(R.string.edit)
                },
                confirmTextColor = when (uiState) {
                    is UIState.AddItem -> MaterialTheme.colorScheme.onPrimary
                    is UIState.EditItem -> MaterialTheme.colorScheme.onPrimary
                    is UIState.ViewItem -> MaterialTheme.colorScheme.onPrimaryContainer
                },
                onConfirmClicked = {
                    focusManager.clearFocus(force = true)

                    if (uiState is UIState.ViewItem) {
                        onEditClicked()
                    } else {
                        offlineBottomSheetVM.idMailState.apply {
                            val isIDMailEmpty = isEmpty() || isBlank()
                            offlineBottomSheetVM.idMailError = isIDMailEmpty

                            if (isIDMailEmpty) {
                                offlineBottomSheetVM.idMailErrorMessage = textfieldError
                            }
                        }

                        offlineBottomSheetVM.passwordState.apply {
                            val isPasswordEmpty = isEmpty() || isBlank()
                            offlineBottomSheetVM.passwordError = isPasswordEmpty

                            if (isPasswordEmpty) {
                                offlineBottomSheetVM.passwordErrorMessage = textfieldError
                            }
                        }

                        if (!(offlineBottomSheetVM.idMailError || offlineBottomSheetVM.passwordError)) {
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