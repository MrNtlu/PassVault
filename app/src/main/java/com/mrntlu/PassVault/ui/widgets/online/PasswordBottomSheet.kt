package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
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
import com.mrntlu.PassVault.ui.theme.*
import com.mrntlu.PassVault.utils.Cryptography
import com.mrntlu.PassVault.utils.SheetState
import com.mrntlu.PassVault.utils.areFieldsEnabled
import com.mrntlu.PassVault.viewmodels.BottomSheetViewModel
import com.mrntlu.PassVault.viewmodels.HomeViewModel

@Composable
fun PasswordBottomSheet(
    homeVM: HomeViewModel,
    sheetState: SheetState<PasswordItem>,
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

    LaunchedEffect(key1 = sheetState) {
        bottomSheetVM.setStateValues(sheetState)
    }

    LaunchedEffect(key1 = isSheetVisible) {
        if (!isSheetVisible && sheetState is SheetState.AddItem) {
            focusManager.clearFocus(force = true)

            titleError = false
            usernameError = false
            passwordError = false

            bottomSheetVM.resetValues()
        }
    }

    //TODO: Add Color Picker
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
            verticalArrangement = Arrangement.Center
        ) {
            PasswordBottomSheetFields(
                bottomSheetVM = bottomSheetVM,
                sheetState = sheetState,
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
                    enabled = sheetState.areFieldsEnabled(),
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

            val colorPickerList = listOf(
                Red500,
                Red700,
                Purple500,
                Purple700,
                Blue500,
                Blue700,
                Green500,
                Green700,
                Yellow500,
                Yellow700,
                Orange500,
                Orange700,
                Brown500,
                Brown700
            )
            //TODO Implement Selection and hold it in mutablestate
            LazyRow(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .imePadding()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    items(
                        count = colorPickerList.size,
                        key = { index ->
                            colorPickerList[index].toString()
                        },
                    ) { index ->
                        val color = colorPickerList[index]

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .border(BorderStroke(2.dp, Yellow600), shape = CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Canvas(
                                modifier = Modifier
                                    .size(19.dp),
                                onDraw = {
                                    drawCircle(color = color)
                                }
                            )
                        }
                    }
                }
            )

            val textfieldError = stringResource(R.string.textfield_error)
            val cryptoKey = stringResource(id = R.string.crypto_key)

            BottomSheetButtons(
                isConfirmButtonAvailable = isNetworkAvailable,
                confirmBGColor = when (sheetState) {
                    is SheetState.AddItem -> BlueLogo
                    is SheetState.EditItem -> BlueLogo
                    is SheetState.ViewItem -> Yellow700
                },
                confirmText = when(sheetState) {
                    is SheetState.AddItem -> stringResource(id = R.string.save)
                    is SheetState.EditItem -> stringResource(id = R.string.update)
                    is SheetState.ViewItem -> stringResource(id = R.string.edit)
                },
                onConfirmClicked = {
                    focusManager.clearFocus(force = true)

                    if (sheetState is SheetState.ViewItem) {
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
                            when(sheetState) {
                                is SheetState.AddItem -> {
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
                                is SheetState.EditItem -> {
                                    onCancel()

                                    val encryptedPassword: String? = if (bottomSheetVM.isEncrypted) {
                                        Cryptography(cryptoKey).encrypt(bottomSheetVM.passwordState)
                                    } else null

                                    homeVM.editPassword(
                                        sheetState.position,
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
                dismissText = when(sheetState) {
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
fun PasswordBottomSheetPreview() {
    PasswordBottomSheet(homeVM = viewModel(), sheetState = SheetState.AddItem, isSheetVisible = true, onEditClicked = {}, onInfoDialogClicked = {}, onCancel = {}, isNetworkAvailable = true)
}

@Preview
@Composable
fun PasswordBottomSheetOfflinePreview() {
    PasswordBottomSheet(homeVM = viewModel(), sheetState = SheetState.AddItem, isSheetVisible = true, onEditClicked = {}, onInfoDialogClicked = {}, onCancel = {}, isNetworkAvailable = false)
}