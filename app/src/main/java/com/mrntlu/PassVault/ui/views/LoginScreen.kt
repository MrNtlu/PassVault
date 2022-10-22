package com.mrntlu.PassVault.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mrntlu.PassVault.ui.theme.BlueMidnight
import com.mrntlu.PassVault.ui.widgets.ErrorDialog
import com.mrntlu.PassVault.utils.CheckLoggedIn
import com.mrntlu.PassVault.viewmodels.auth.FirebaseAuthViewModel
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    firebaseVM: FirebaseAuthViewModel,
    parseVM: ParseAuthViewModel
) {
    val isErrorOccured = parseVM.isErrorOccured.value

    CheckLoggedIn(navController = navController, firebaseVM = firebaseVM, parseVM = parseVM)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val focusManager = LocalFocusManager.current

        val usernameState = remember { mutableStateOf(TextFieldValue()) }
        val passwordState = remember { mutableStateOf(TextFieldValue()) }
        var passwordVisible by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.width(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = usernameState.value,
                onValueChange = { usernameState.value = it },
                modifier = Modifier.padding(8.dp),
                singleLine = true,
                label = {
                    Text(text = "Username/Email")
                }
            )

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                modifier = Modifier.padding(8.dp),
                singleLine = true,
                label = {
                    Text(text = "Password")
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, description)
                    }
                }
            )

            Button(
                onClick = {
                    focusManager.clearFocus(force = true)
                    parseVM.parseLogin(usernameState.value.text, passwordState.value.text)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .padding(top = 12.dp),
            ) {
                Text(text = "Login")
            }

            TextButton(
                onClick = { navController.navigate("register") },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = BlueMidnight
                )
            ) {
                Text(text = "Don't have an account? Register")
            }

            if(isErrorOccured != null) {
                var showDialog by remember { mutableStateOf(true)  }

                if (showDialog) {
                    ErrorDialog(error = isErrorOccured) {
                        showDialog = false
                        parseVM.isErrorOccured.value = null
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController(), viewModel(), viewModel())
}