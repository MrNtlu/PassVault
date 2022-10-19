package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrntlu.PassVault.R

@Composable
fun PasswordBottomSheet(
    onCancel: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    val titleState = remember { mutableStateOf(TextFieldValue()) }
    val usernameState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by remember { mutableStateOf(false) }
    val notesState = remember { mutableStateOf(TextFieldValue()) }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.width(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = titleState.value,
                onValueChange = { titleState.value = it },
                modifier = Modifier.padding(8.dp),
                singleLine = true,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_turned_in_black_24dp),
                        contentDescription = "Title Leading"
                    )
                },
                label = {
                    Text(text = "Title")
                }
            )

            OutlinedTextField(
                value = usernameState.value,
                onValueChange = { usernameState.value = it },
                modifier = Modifier.padding(8.dp),
                singleLine = true,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_account_circle_black_24dp),
                        contentDescription = "Account Leading"
                    )
                },
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
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_padlock_pass),
                        contentDescription = "Password Leading"
                    )
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

            OutlinedTextField(
                value = notesState.value,
                onValueChange = { notesState.value = it },
                modifier = Modifier.padding(8.dp),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_description_black_24dp),
                        contentDescription = "Notes Leading"
                    )
                },
                label = {
                    Text(text = "Notes")
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        focusManager.clearFocus(force = true)

                        //TODO Save inputs
                    },
                ) {
                    Text(text = "Save")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        focusManager.clearFocus(force = true)
                        onCancel()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.DarkGray,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}

@Preview
@Composable
fun PasswordBottomSheetPreview() {
    PasswordBottomSheet(onCancel = {})
}