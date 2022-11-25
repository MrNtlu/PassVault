package com.mrntlu.PassVault.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.models.UserRegister
import com.mrntlu.PassVault.ui.widgets.ErrorDialog
import com.mrntlu.PassVault.utils.setTextfieldTheme
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    parseVM: ParseAuthViewModel
) {
    val focusManager = LocalFocusManager.current
    val isErrorOccured = parseVM.isErrorOccured.value
    val isRegistered = parseVM.isRegistered.value

    val usernameState = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }
    val emailState = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }
    val passwordState = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }
    var passwordVisible by remember { mutableStateOf(false) }
    var privacyPolicy by rememberSaveable { mutableStateOf(false) }
    var termsConditions by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = usernameState.value,
                onValueChange = { usernameState.value = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(FocusDirection.Down) }),
                label = {
                    Text(text = stringResource(R.string.username))
                },
                colors = TextFieldDefaults.setTextfieldTheme(),
            )

            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 3.dp)
                    .fillMaxWidth(),
                value = emailState.value,
                onValueChange = { emailState.value = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
                keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(FocusDirection.Down) }),
                label = {
                    Text(text = stringResource(R.string.email))
                },
                colors = TextFieldDefaults.setTextfieldTheme(),
            )

            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 3.dp)
                    .fillMaxWidth(),
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                singleLine = true,
                label = {
                    Text(text = stringResource(id = R.string.password))
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible)
                        stringResource(id = R.string.hide_password)
                    else
                        stringResource(id = R.string.show_password)

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            description,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                colors = TextFieldDefaults.setTextfieldTheme(),
            )

            Row(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = termsConditions,
                    onCheckedChange = { termsConditions = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.outline,
                    ),
                )

                TextButton(
                    modifier = Modifier
                        .weight(1f),
                    onClick = { navController.navigate("policy/${true}") },
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.terms_conditions_),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = privacyPolicy,
                    onCheckedChange = { privacyPolicy = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.outline,
                    ),
                )

                TextButton(
                    modifier = Modifier
                        .weight(1f),
                    onClick = { navController.navigate("policy/${false}") },
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.privacy_policy_),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }

            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                onClick = {
                    focusManager.clearFocus(force = true)
                    parseVM.parseRegister(
                        UserRegister(
                            usernameState.value.text,
                            emailState.value.text,
                            passwordState.value.text
                        ),
                        isPolicyChecked = privacyPolicy,
                        isTermsChecked = termsConditions,
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }

            TextButton(
                onClick = { navController.popBackStack() },
            ) {
                Text(
                    text = stringResource(R.string.have_acc_login),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            if(isErrorOccured != null) {
                val showDialog = remember { mutableStateOf(true)  }

                if (showDialog.value) {
                    ErrorDialog(error = isErrorOccured) {
                        showDialog.value = false
                        parseVM.isErrorOccured.value = null
                    }
                }
            }

            if (isRegistered) {
                parseVM.isRegistered.value = false
                navController.popBackStack()
            }
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(rememberNavController(), viewModel())
}