package com.mrntlu.PassVault.ui.widgets.offline

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import android.widget.Toast
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
import com.mrntlu.PassVault.utils.UIState
import com.mrntlu.PassVault.utils.areFieldsEnabled
import com.mrntlu.PassVault.utils.setTextfieldTheme
import com.mrntlu.PassVault.viewmodels.offline.OfflineBottomSheetViewModel

@Composable
fun OfflineBottomSheetFields(
    offlineBottomSheetVM: OfflineBottomSheetViewModel,
    uiState: UIState<OfflinePassword>,
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
            keyboardType = KeyboardType.Text,
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(FocusDirection.Down) }),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_turned_in_black_24dp),
                contentDescription = stringResource(R.string.cd_mail_username)
            )
        },
        trailingIcon = {
            if (uiState is UIState.ViewItem) {
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(offlineBottomSheetVM.idMailState))

                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                            Toast
                                .makeText(
                                    context,
                                    "${offlineBottomSheetVM.idMailState} Copied",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }

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
            Text(text = stringResource(R.string.title_username_email))
        },
        enabled = uiState.areFieldsEnabled(),
        isError = offlineBottomSheetVM.idMailError,
        errorMsg = offlineBottomSheetVM.idMailErrorMessage,
        colors = TextFieldDefaults.setTextfieldTheme(),
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
            Icon(
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

                if (uiState is UIState.ViewItem) {
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clipData = ClipData.newPlainText("Password", offlineBottomSheetVM.passwordState)

                            clipData.description.extras = PersistableBundle().apply {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                                } else {
                                    putBoolean("android.content.extra.IS_SENSITIVE", true)
                                }
                            }
                            clipboard.setPrimaryClip(clipData)

                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                                Toast
                                    .makeText(
                                        context,
                                        "${offlineBottomSheetVM.idMailState} Copied",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ContentCopy,
                            contentDescription = stringResource(id = R.string.cd_copy),
                        )
                    }
                }
            }
        },
        enabled = uiState.areFieldsEnabled(),
        isError = offlineBottomSheetVM.passwordError,
        errorMsg = offlineBottomSheetVM.passwordErrorMessage,
        colors = TextFieldDefaults.setTextfieldTheme(),
    )

    OutlinedTextField(
        value = offlineBottomSheetVM.descriptionState,
        onValueChange = { offlineBottomSheetVM.descriptionState = it },
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_description_black_24dp),
                contentDescription = stringResource(R.string.cd_description),
            )
        },
        maxLines = 3,
        enabled = uiState.areFieldsEnabled(),
        label = {
            Text(text = stringResource(id = R.string.description))
        },
        colors = TextFieldDefaults.setTextfieldTheme(),
    )
}