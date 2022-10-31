package com.mrntlu.PassVault.ui.widgets

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
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextFieldWithErrorView(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(vertical = 3.dp)
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
            .padding(vertical = 3.dp)
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
        label = {
            Text(text = stringResource(id = R.string.username_mail))
        },
        enabled = sheetState.areFieldsEnabled(),
        isError = usernameError,
        errorMsg = usernameErrorMessage
    )

    OutlinedTextFieldWithErrorView(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(vertical = 3.dp)
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

            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(imageVector  = image, description)
            }
        },
        enabled = sheetState.areFieldsEnabled(),
        isError = passwordError,
        errorMsg = passwordErrorMessage
    )

    OutlinedTextField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(vertical = 3.dp)
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
        }
    )
}