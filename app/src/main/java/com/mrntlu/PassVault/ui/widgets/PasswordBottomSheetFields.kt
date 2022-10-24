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
        enabled = sheetState.areFieldsEnabled(),
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
        enabled = sheetState.areFieldsEnabled(),
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
        enabled = sheetState.areFieldsEnabled(),
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
        enabled = sheetState.areFieldsEnabled(),
        label = {
            Text(text = "Notes")
        }
    )
}