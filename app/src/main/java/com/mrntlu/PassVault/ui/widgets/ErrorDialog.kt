package com.mrntlu.PassVault.ui.widgets

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mrntlu.PassVault.R

@Composable
fun ErrorDialog(
    error: String,
    onDismissClicked: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissClicked,
        title = {
            Text(text = stringResource(R.string.error))
        },
        text = {
            Text(error)
        },
        confirmButton = {},
        dismissButton = {
            Button(onClick = onDismissClicked) {
                Text(stringResource(R.string.ok_))
            }
        }
    )
}

@Preview
@Composable
fun ErrorDialogPreview() {
    ErrorDialog("Error") {}
}