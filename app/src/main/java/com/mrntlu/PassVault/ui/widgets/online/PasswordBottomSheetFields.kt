package com.mrntlu.PassVault.ui.widgets

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.utils.SheetState
import com.mrntlu.PassVault.utils.areFieldsEnabled
import com.mrntlu.PassVault.viewmodels.BottomSheetViewModel

@Composable
fun PasswordBottomSheetFields(
    bottomSheetVM: BottomSheetViewModel,
    sheetState: SheetState<PasswordItem>,
    titleError: Boolean,
    titleErrorMessage: String,
    usernameError: Boolean,
    usernameErrorMessage: String,
    passwordError: Boolean,
    passwordErrorMessage: String,
) {
    val clipboardManager = LocalClipboardManager.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextFieldWithErrorView(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = bottomSheetVM.titleState,
        onValueChange = { bottomSheetVM.titleState = it },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(FocusDirection.Down) }),
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_turned_in_black_24dp),
                contentDescription = stringResource(R.string.cd_title)
            )
        },
        label = {
            Text(text = stringResource(id = R.string.title))
        },
        enabled = sheetState.areFieldsEnabled(),
        isError = titleError,
        errorMsg = titleErrorMessage
    )

    OutlinedTextFieldWithErrorView(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = bottomSheetVM.usernameState,
        onValueChange = { bottomSheetVM.usernameState = it },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
        keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(FocusDirection.Down) }),
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_account_circle_black_24dp),
                contentDescription = stringResource(R.string.cd_account)
            )
        },
        trailingIcon = {
            if (sheetState is SheetState.ViewItem) {
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(bottomSheetVM.usernameState))
                        Toast
                            .makeText(
                                context,
                                "${bottomSheetVM.usernameState} Coppied",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ContentCopy,
                        contentDescription = stringResource(id = R.string.cd_copy)
                    )
                }
            }
        },
        label = {
            Text(text = stringResource(id = R.string.username_mail))
        },
        enabled = sheetState.areFieldsEnabled(),
        isError = usernameError,
        errorMsg = usernameErrorMessage,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = Color.Black,
            disabledTrailingIconColor = Color.Black,
            trailingIconColor = Color.Black,
        )
    )

    OutlinedTextFieldWithErrorView(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = bottomSheetVM.passwordState,
        onValueChange = { bottomSheetVM.passwordState = it },
        singleLine = true,
        label = {
            Text(text = stringResource(id = R.string.password))
        },
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_padlock_pass),
                contentDescription = stringResource(id = R.string.cd_password)
            )
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (passwordVisible)
                stringResource(id = R.string.hide_password)
            else
                stringResource(id = R.string.show_password)

            Row {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }

                if (sheetState is SheetState.ViewItem) {
                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(bottomSheetVM.passwordState))
                            Toast
                                .makeText(
                                    context,
                                    "${bottomSheetVM.titleState} Coppied",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ContentCopy,
                            contentDescription = stringResource(id = R.string.cd_copy)
                        )
                    }
                }
            }
        },
        enabled = sheetState.areFieldsEnabled(),
        isError = passwordError,
        errorMsg = passwordErrorMessage,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = Color.Black,
            disabledTrailingIconColor = Color.Black,
            trailingIconColor = Color.Black,
        ),
    )

    OutlinedTextField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = bottomSheetVM.noteState,
        onValueChange = { bottomSheetVM.noteState = it },
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_description_black_24dp),
                contentDescription = stringResource(R.string.cd_note)
            )
        },
        enabled = sheetState.areFieldsEnabled(),
        label = {
            Text(text = stringResource(id = R.string.notes))
        },
        maxLines = 3,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = Color.Black,
        )
    )
}