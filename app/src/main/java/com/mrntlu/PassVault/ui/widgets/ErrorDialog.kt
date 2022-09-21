package com.mrntlu.PassVault.ui.widgets

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ErrorDialog(
    error: String,
    onDismissClicked: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissClicked,
        title = {
            Text(text = "Error")
        },
        text = {
            Text(error)
        },
        confirmButton = {},
        dismissButton = {
            Button(onClick = onDismissClicked) {
                Text("OK!")
            }
        }
    )
}

@Preview
@Composable
fun ErrorDialogPreview() {
    ErrorDialog("Error", {})
}