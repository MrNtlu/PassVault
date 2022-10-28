package com.mrntlu.PassVault.ui.widgets.offline

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextFieldWithErrorView(
        value = offlineBottomSheetVM.idMailState,
        onValueChange = { offlineBottomSheetVM.idMailState = it },
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
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = stringResource(R.string.cd_mail_username)
            )
        },
        label = {
            Text(text = stringResource(id = R.string.username_mail))
        },
        enabled = sheetState.areFieldsEnabled(),
        isError = idMailError,
        errorMsg = idMailErrorMessage
    )

    OutlinedTextFieldWithErrorView(
        value = offlineBottomSheetVM.passwordState,
        onValueChange = { offlineBottomSheetVM.passwordState = it },
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(vertical = 3.dp)
            .fillMaxWidth(),
        singleLine = true,
        label = {
            Text(text = stringResource(id = R.string.password))
        },
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_padlock_pass),
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

            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(imageVector  = image, description)
            }
        },
        enabled = sheetState.areFieldsEnabled(),
        isError = passwordError,
        errorMsg = passwordErrorMessage
    )

    OutlinedTextField(
        value = offlineBottomSheetVM.descriptionState,
        onValueChange = { offlineBottomSheetVM.descriptionState = it },
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(vertical = 3.dp)
            .fillMaxWidth(),
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_description_black_24dp),
                contentDescription = stringResource(R.string.cd_description),
            )
        },
        maxLines = 5,
        enabled = sheetState.areFieldsEnabled(),
        label = {
            Text(text = stringResource(id = R.string.description))
        }
    )
}