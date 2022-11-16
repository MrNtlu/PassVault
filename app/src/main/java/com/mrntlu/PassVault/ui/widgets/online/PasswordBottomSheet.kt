package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.ui.theme.BlueLogo
import com.mrntlu.PassVault.ui.theme.Purple500
import com.mrntlu.PassVault.ui.theme.Yellow700
import com.mrntlu.PassVault.utils.Cryptography
import com.mrntlu.PassVault.utils.UIState
import com.mrntlu.PassVault.utils.areFieldsEnabled
import com.mrntlu.PassVault.viewmodels.BottomSheetViewModel
import com.mrntlu.PassVault.viewmodels.online.HomeViewModel

@Composable
fun PasswordBottomSheet(
    homeVM: HomeViewModel,
    uiState: UIState<PasswordItem>,
    isSheetVisible: Boolean,
    isNetworkAvailable: Boolean,
    onEditClicked: () -> Unit,
    onInfoDialogClicked: () -> Unit,
    onCancel: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val bottomSheetVM by remember { mutableStateOf(BottomSheetViewModel()) }

    var titleError by remember { mutableStateOf(false) }
    var titleErrorMessage by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf(false) }
    var usernameErrorMessage by remember { mutableStateOf("") }

    var passwordError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = uiState) {
        bottomSheetVM.setStateValues(uiState)
    }

    LaunchedEffect(key1 = isSheetVisible) {
        if (!isSheetVisible && uiState is UIState.AddItem) {
            focusManager.clearFocus(force = true)

            titleError = false
            usernameError = false
            passwordError = false

            bottomSheetVM.resetValues()
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
                .padding(top = 16.dp)
                .imePadding()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState !is UIState.ViewItem) {
                ColorPickerRow(
                    bottomSheetVM = bottomSheetVM,
                )
            }

            PasswordBottomSheetFields(
                bottomSheetVM = bottomSheetVM,
                uiState = uiState,
                titleError, titleErrorMessage, usernameError, usernameErrorMessage, passwordError, passwordErrorMessage
            )

            Row(
                modifier = Modifier
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = bottomSheetVM.isEncrypted,
                    onCheckedChange = { bottomSheetVM.isEncrypted = it },
                    enabled = uiState.areFieldsEnabled(),
                    colors = CheckboxDefaults.colors(
                        checkedColor = BlueLogo
                    )
                )

                Text(
                    text = stringResource(R.string.encryption_info),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                IconButton(
                    onClick = onInfoDialogClicked,
                ) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp),
                        imageVector = Icons.Rounded.Info,
                        contentDescription = stringResource(R.string.cd_what_encryption),
                        tint = Purple500,
                    )
                }
            }

            val textfieldError = stringResource(R.string.textfield_error)
            val cryptoKey = stringResource(id = R.string.crypto_key)

            BottomSheetButtons(
                isConfirmButtonAvailable = isNetworkAvailable,
                confirmBGColor = when (uiState) {
                    is UIState.AddItem -> BlueLogo
                    is UIState.EditItem -> BlueLogo
                    is UIState.ViewItem -> Yellow700
                },
                confirmText = when(uiState) {
                    is UIState.AddItem -> stringResource(id = R.string.save)
                    is UIState.EditItem -> stringResource(id = R.string.update)
                    is UIState.ViewItem -> stringResource(id = R.string.edit)
                },
                onConfirmClicked = {
                    focusManager.clearFocus(force = true)

                    if (uiState is UIState.ViewItem) {
                        onEditClicked()
                    } else {
                        bottomSheetVM.titleState.apply {
                            val isTitleEmpty = isEmpty() || isBlank()
                            titleError = isTitleEmpty

                            if (isTitleEmpty) {
                                titleErrorMessage = textfieldError
                            }
                        }

                        bottomSheetVM.usernameState.apply {
                            val isUsernameEmpty = isEmpty() || isBlank()
                            usernameError = isUsernameEmpty

                            if (isUsernameEmpty) {
                                usernameErrorMessage = textfieldError
                            }
                        }

                        bottomSheetVM.passwordState.apply {
                            val isPasswordEmpty = isEmpty() || isBlank()
                            passwordError = isPasswordEmpty

                            if (isPasswordEmpty) {
                                passwordErrorMessage = textfieldError
                            }
                        }

                        if (!(titleError || usernameError || passwordError)) {
                            when(uiState) {
                                is UIState.AddItem -> {
                                    onCancel()

                                    val encryptedPassword: String? = if (bottomSheetVM.isEncrypted) {
                                        Cryptography(cryptoKey).encrypt(bottomSheetVM.passwordState)
                                    } else null

                                    homeVM.addPassword(
                                        bottomSheetVM.titleState,
                                        bottomSheetVM.usernameState,
                                        encryptedPassword ?: bottomSheetVM.passwordState,
                                        bottomSheetVM.noteState,
                                        bottomSheetVM.isEncrypted
                                    )
                                }
                                is UIState.EditItem -> {
                                    onCancel()

                                    val encryptedPassword: String? = if (bottomSheetVM.isEncrypted) {
                                        Cryptography(cryptoKey).encrypt(bottomSheetVM.passwordState)
                                    } else null

                                    homeVM.editPassword(
                                        uiState.position,
                                        bottomSheetVM.titleState,
                                        bottomSheetVM.usernameState,
                                        encryptedPassword ?: bottomSheetVM.passwordState,
                                        bottomSheetVM.noteState,
                                        bottomSheetVM.isEncrypted
                                    )
                                }
                                else -> {}
                            }
                        }
                    }
                },
                dismissText = when(uiState) {
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
fun PasswordBottomSheetPreview() {
    PasswordBottomSheet(homeVM = viewModel(), uiState = UIState.AddItem, isSheetVisible = true, onEditClicked = {}, onInfoDialogClicked = {}, onCancel = {}, isNetworkAvailable = true)
}

@Preview
@Composable
fun PasswordBottomSheetOfflinePreview() {
    PasswordBottomSheet(homeVM = viewModel(), uiState = UIState.AddItem, isSheetVisible = true, onEditClicked = {}, onInfoDialogClicked = {}, onCancel = {}, isNetworkAvailable = false)
}