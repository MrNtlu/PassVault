package com.mrntlu.PassVault.ui.widgets.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.ui.theme.Purple500
import com.mrntlu.PassVault.ui.widgets.BottomSheetButtons
import com.mrntlu.PassVault.ui.widgets.OutlinedTextFieldWithErrorView
import com.mrntlu.PassVault.utils.isValidEmail
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel

@Composable
fun ForgotPasswordSheet(
    parseVM: ParseAuthViewModel,
    isSheetVisible: Boolean,
    onCancel: () -> Unit,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var emailState by remember{ mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var emailErrorMessage by remember { mutableStateOf("") }
    val textfieldError = stringResource(R.string.email_not_valid)
    val successMessage = stringResource(R.string.forgot_password_success)

    LaunchedEffect(key1 = isSheetVisible) {
        if (!isSheetVisible) {
            focusManager.clearFocus(force = true)

            emailState = ""
            emailError = false
            emailErrorMessage = ""
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 48.dp)
            .padding(bottom = 8.dp)
            .padding(top = 8.dp)
            .imePadding()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextFieldWithErrorView(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(vertical = 3.dp)
                .fillMaxWidth(),
            value = emailState,
            onValueChange = { emailState = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email,
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            leadingIcon = {
                Image(
                    imageVector = Icons.Rounded.Email,
                    contentDescription = stringResource(R.string.cd_email)
                )
            },
            label = {
                Text(text = stringResource(id = R.string.email))
            },
            isError = emailError,
            errorMsg = emailErrorMessage,
        )

        BottomSheetButtons(
            confirmBGColor = Purple500,
            confirmText = stringResource(id = R.string.send),
            onConfirmClicked = {
                focusManager.clearFocus(force = true)

                if (emailState.isValidEmail()) {
                    emailError = false
                    emailErrorMessage = ""

                    parseVM.parseForgotPassword(
                        email = emailState,
                        onSuccess = {
                            onCancel()
                            Toast.makeText(context, successMessage, Toast.LENGTH_LONG).show()
                        }
                    )
                } else {
                    emailError = true
                    emailErrorMessage = textfieldError
                }
            },
            dismissText = stringResource(id = R.string.cancel),
            onDismissClicked = {
                focusManager.clearFocus(force = true)
                onCancel()
            }
        )
    }
}