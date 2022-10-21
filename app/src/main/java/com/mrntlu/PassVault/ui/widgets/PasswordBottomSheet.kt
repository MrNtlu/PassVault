package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.viewmodels.BottomSheetViewModel
import com.mrntlu.PassVault.viewmodels.HomeViewModel

sealed class SheetState<out T> {
    object AddItem: SheetState<Nothing>()

    data class EditItem<out T>(
        val item: T
    ): SheetState<T>()

    data class ViewItem<out T>(
        val item: T
    ): SheetState<T>()
}

fun <T> SheetState<T>.getItem(): T? = when(this) {
    is SheetState.EditItem -> item
    is SheetState.ViewItem -> item
    is SheetState.AddItem -> null
}

@Composable
fun PasswordBottomSheet(
    homeVM: HomeViewModel,
    sheetState: SheetState<PasswordItem>,
    onCancel: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val bottomSheetVM by remember { mutableStateOf(BottomSheetViewModel()) }

    LaunchedEffect(key1 = sheetState) {
        bottomSheetVM.setStateValues(sheetState)
    }

    var titleError by remember { mutableStateOf(false) }
    var titleErrorMessage by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf(false) }
    var usernameErrorMessage by remember { mutableStateOf("") }

    var passwordError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 36.dp)
            .padding(bottom = 8.dp)
            .padding(top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextFieldWithErrorView(
                value = bottomSheetVM.titleState,
                onValueChange = { bottomSheetVM.titleState = it },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 3.dp)
                    .fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(FocusDirection.Down) }),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_turned_in_black_24dp),
                        contentDescription = "Title Leading"
                    )
                },
                label = {
                    Text(text = "Title")
                },
                isError = titleError,
                errorMsg = titleErrorMessage
            )

            OutlinedTextFieldWithErrorView(
                value = bottomSheetVM.usernameState,
                onValueChange = { bottomSheetVM.usernameState = it },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 3.dp)
                    .fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
                keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(FocusDirection.Down) }),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_account_circle_black_24dp),
                        contentDescription = "Account Leading"
                    )
                },
                label = {
                    Text(text = "Username/Email")
                },
                isError = usernameError,
                errorMsg = usernameErrorMessage
            )

            OutlinedTextFieldWithErrorView(
                value = bottomSheetVM.passwordState,
                onValueChange = { bottomSheetVM.passwordState = it },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 3.dp)
                    .fillMaxWidth(),
                singleLine = true,

                label = {
                    Text(text = "Password")
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_padlock_pass),
                        contentDescription = "Password Leading"
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, description)
                    }
                },
                isError = passwordError,
                errorMsg = passwordErrorMessage
            )

            OutlinedTextField(
                value = bottomSheetVM.noteState,
                onValueChange = { bottomSheetVM.noteState = it },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 3.dp)
                    .fillMaxWidth(),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_description_black_24dp),
                        contentDescription = "Notes Leading"
                    )
                },
                label = {
                    Text(text = "Notes")
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        focusManager.clearFocus(force = true)

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

                                    //TODO Edit
                                }
                                is SheetState.ViewItem -> {
                                    //TODO Change state
                                }
                            }
                        }
                    },
                ) {
                    Text(text = when(sheetState) {
                        is SheetState.AddItem -> "Save"
                        is SheetState.EditItem -> "Update"
                        is SheetState.ViewItem -> "Edit"
                    })
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        focusManager.clearFocus(force = true)
                        onCancel()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.DarkGray,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}

@Preview
@Composable
fun PasswordBottomSheetPreview() {
    PasswordBottomSheet(homeVM = viewModel(), sheetState = SheetState.AddItem, onCancel = {})
}