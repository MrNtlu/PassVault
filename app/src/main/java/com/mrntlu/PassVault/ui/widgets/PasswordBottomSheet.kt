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
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.utils.printLog
import com.mrntlu.PassVault.viewmodels.HomeViewModel

@Composable
fun PasswordBottomSheet(
    homeVM: HomeViewModel,
    onCancel: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val titleState = remember { mutableStateOf(TextFieldValue()) }
    var titleError by remember { mutableStateOf(false) }
    var titleErrorMessage by remember { mutableStateOf("") }

    val usernameState = remember { mutableStateOf(TextFieldValue()) }
    var usernameError by remember { mutableStateOf(false) }
    var usernameErrorMessage by remember { mutableStateOf("") }

    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    var passwordError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val notesState = remember { mutableStateOf(TextFieldValue()) }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.width(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextFieldWithErrorView(
                value = titleState.value,
                onValueChange = { titleState.value = it },
                modifier = Modifier.padding(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Text),
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
                value = usernameState.value,
                onValueChange = { usernameState.value = it },
                modifier = Modifier.padding(8.dp),
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
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                modifier = Modifier.padding(8.dp),
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
                value = notesState.value,
                onValueChange = { notesState.value = it },
                modifier = Modifier.padding(8.dp),
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

                        titleState.value.text.apply {
                            val isTitleEmpty = isEmpty() || isBlank()
                            titleError = isTitleEmpty

                            if (isTitleEmpty) {
                                titleErrorMessage = "Please don't leave empty"
                            }
                        }

                        usernameState.value.text.apply {
                            val isUsernameEmpty = isEmpty() || isBlank()
                            usernameError = isUsernameEmpty

                            if (isUsernameEmpty) {
                                usernameErrorMessage = "Please don't leave empty"
                            }
                        }

                        passwordState.value.text.apply {
                            val isPasswordEmpty = isEmpty() || isBlank()
                            passwordError = isPasswordEmpty

                            if (isPasswordEmpty) {
                                passwordErrorMessage = "Please don't leave empty"
                            }
                        }

                        if (!(titleError || usernameError || passwordError)) {
                            printLog(message = usernameState.value.text)
                            printLog(message = titleState.value.text)
                            printLog(message = passwordState.value.text)
                            printLog(message = notesState.value.text)

                            homeVM.addPassword(
                                titleState.value.text,
                                usernameState.value.text,
                                passwordState.value.text,
                                notesState.value.text
                            )
                        }
                    },
                ) {
                    Text(text = "Save")
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
    PasswordBottomSheet(homeVM = viewModel(), onCancel = {})
}