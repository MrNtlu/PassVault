package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.ui.theme.BlueLogo
import com.mrntlu.PassVault.ui.theme.Purple500
import com.mrntlu.PassVault.ui.theme.Yellow700
import com.mrntlu.PassVault.utils.SheetState
import com.mrntlu.PassVault.utils.areFieldsEnabled
import com.mrntlu.PassVault.viewmodels.BottomSheetViewModel
import com.mrntlu.PassVault.viewmodels.HomeViewModel

@Composable
fun PasswordBottomSheet(
    homeVM: HomeViewModel,
    sheetState: SheetState<PasswordItem>,
    onEditClicked: () -> Unit,
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

        titleError = false
        usernameError = false
        passwordError = false
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 36.dp)
            .padding(bottom = 8.dp)
            .padding(top = 16.dp)
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PasswordBottomSheetFields(
                bottomSheetVM = bottomSheetVM,
                sheetState = sheetState,
                titleError, titleErrorMessage, usernameError, usernameErrorMessage, passwordError, passwordErrorMessage
            )

            Row(
                modifier = Modifier.padding(top = 4.dp),
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
                    text = "Encryption (Recommended)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                IconButton(
                    onClick = { /*TODO Open Dialog*/ },
                ) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp),
                        imageVector = Icons.Rounded.Info,
                        contentDescription = "What is encryption?",
                        tint = Purple500,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = when(sheetState) {
                            is SheetState.AddItem -> BlueLogo
                            is SheetState.EditItem -> BlueLogo
                            is SheetState.ViewItem -> Yellow700
                        }
                    ),
                    onClick = {
                        focusManager.clearFocus(force = true)

                        if (sheetState is SheetState.ViewItem) {
                            onEditClicked()
                        } else {
                            bottomSheetVM.titleState.apply {
                                val isTitleEmpty = isEmpty() || isBlank()
                                titleError = isTitleEmpty

                                if (isTitleEmpty) {
                                    titleErrorMessage = "Please don't leave empty"
                                }
                            }

                            bottomSheetVM.usernameState.apply {
                                val isUsernameEmpty = isEmpty() || isBlank()
                                usernameError = isUsernameEmpty

                                if (isUsernameEmpty) {
                                    usernameErrorMessage = "Please don't leave empty"
                                }
                            }

                            bottomSheetVM.passwordState.apply {
                                val isPasswordEmpty = isEmpty() || isBlank()
                                passwordError = isPasswordEmpty

                                if (isPasswordEmpty) {
                                    passwordErrorMessage = "Please don't leave empty"
                                }
                            }

                            if (!(titleError || usernameError || passwordError)) {
                                when(sheetState) {
                                    is SheetState.AddItem -> {
                                        onCancel()

                                        homeVM.addPassword(
                                            bottomSheetVM.titleState,
                                            bottomSheetVM.usernameState,
                                            bottomSheetVM.passwordState,
                                            bottomSheetVM.noteState
                                        )
                                    }
                                    is SheetState.EditItem -> {
                                        onCancel()

                                        homeVM.editPassword(
                                            sheetState.position,
                                            bottomSheetVM.titleState,
                                            bottomSheetVM.usernameState,
                                            bottomSheetVM.passwordState,
                                            bottomSheetVM.noteState
                                        )
                                    }
                                    else -> {}
                                }
                            }
                        }
                    },
                ) {
                    Text(
                        text = when(sheetState) {
                            is SheetState.AddItem -> "Save"
                            is SheetState.EditItem -> "Update"
                            is SheetState.ViewItem -> "Edit"
                        },
                        color = Color.White,
                    )
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        focusManager.clearFocus(force = true)
                        onCancel()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.DarkGray,
                        contentColor = Color.White,
                    )
                ) {
                    Text(
                        text = when(sheetState) {
                            is SheetState.AddItem -> "Cancel"
                            is SheetState.EditItem -> "Cancel"
                            is SheetState.ViewItem -> "Close"
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PasswordBottomSheetPreview() {
    PasswordBottomSheet(homeVM = viewModel(), sheetState = SheetState.AddItem, onEditClicked = {}, onCancel = {})
}