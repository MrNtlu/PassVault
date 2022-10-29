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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mrntlu.PassVault.R
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

    //TODO: Redesign & add forgot password etc.
    /*
    if (!emailSend.getText().toString().trim().equals("") && emailSend.getText().toString().contains("@")){
        ParseUser.requestPasswordResetInBackground(emailSend.getText().toString(), e -> {
            if (e==null){
                Toasty.info(view1.getContext(),"Email sent to "+emailSend.getText().toString(),Toast.LENGTH_SHORT).show();
            }else {
                if (e.getMessage()!=null)
                    Toasty.error(view1.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            dialog.dismiss();
        });
    }else{
        Toasty.error(view1.getContext(),"Invalid mail.",Toast.LENGTH_SHORT).show();
    }
    */
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
                    Text(text = stringResource(id = R.string.username_mail))
                }
            )

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                modifier = Modifier.padding(8.dp),
                singleLine = true,
                label = {
                    Text(text = stringResource(id = R.string.password))
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible)
                        stringResource(R.string.hide_password)
                    else
                        stringResource(R.string.show_password)

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
                Text(text = stringResource(id = R.string.login))
            }

            TextButton(
                onClick = { navController.navigate("register") },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = BlueMidnight
                )
            ) {
                Text(text = stringResource(R.string.no_acc_register))
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