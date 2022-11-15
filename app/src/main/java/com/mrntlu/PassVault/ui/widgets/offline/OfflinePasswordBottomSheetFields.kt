package com.mrntlu.PassVault.ui.widgets.offline

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
import androidx.compose.material.icons.rounded.Password
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
import com.mrntlu.PassVault.models.OfflinePassword
import com.mrntlu.PassVault.ui.widgets.OutlinedTextFieldWithErrorView
import com.mrntlu.PassVault.utils.SheetState
import com.mrntlu.PassVault.utils.areFieldsEnabled
import com.mrntlu.PassVault.viewmodels.offline.OfflineBottomSheetViewModel

@Composable
fun OfflineBottomSheetFields(
    offlineBottomSheetVM: OfflineBottomSheetViewModel,
    sheetState: SheetState<OfflinePassword>,
    idMailError: Boolean,
    idMailErrorMessage: String,
    passwordError: Boolean,
    passwordErrorMessage: String,
) {
    val clipboardManager = LocalClipboardManager.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextFieldWithErrorView(
        value = offlineBottomSheetVM.idMailState,
        onValueChange = { offlineBottomSheetVM.idMailState = it },
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(FocusDirection.Down) }),
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = stringResource(R.string.cd_mail_username)
            )
        },
        trailingIcon = {
            if (sheetState is SheetState.ViewItem) {
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(offlineBottomSheetVM.idMailState))
                        Toast
                            .makeText(
                                context,
                                "${offlineBottomSheetVM.idMailState} Coppied",
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
        isError = idMailError,
        errorMsg = idMailErrorMessage,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = Color.Black,
            disabledTrailingIconColor = Color.Black,
            trailingIconColor = Color.Black,
        )
    )

    OutlinedTextFieldWithErrorView(
        value = offlineBottomSheetVM.passwordState,
        onValueChange = { offlineBottomSheetVM.passwordState = it },
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        singleLine = true,
        label = {
            Text(text = stringResource(id = R.string.password))
        },
        leadingIcon = {
            Image(
                imageVector = Icons.Rounded.Password,
                contentDescription = stringResource(R.string.cd_password)
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
                            clipboardManager.setText(AnnotatedString(offlineBottomSheetVM.passwordState))
                            Toast
                                .makeText(
                                    context,
                                    "${offlineBottomSheetVM.idMailState} Coppied",
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
        )
    )

    OutlinedTextField(
        value = offlineBottomSheetVM.descriptionState,
        onValueChange = { offlineBottomSheetVM.descriptionState = it },
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_description_black_24dp),
                contentDescription = stringResource(R.string.cd_description),
            )
        },
        maxLines = 3,
        enabled = sheetState.areFieldsEnabled(),
        label = {
            Text(text = stringResource(id = R.string.description))
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = Color.Black,
        )
    )
}