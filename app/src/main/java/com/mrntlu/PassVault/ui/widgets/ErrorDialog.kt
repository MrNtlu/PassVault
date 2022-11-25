package com.mrntlu.PassVault.ui.widgets

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
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
        backgroundColor = MaterialTheme.colorScheme.error,
        onDismissRequest = onDismissClicked,
        title = {
            Text(
                text = stringResource(R.string.error),
                color = MaterialTheme.colorScheme.onError,
            )
        },
        text = {
            Text(
                error,
                color = MaterialTheme.colorScheme.onError,
            )
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = onDismissClicked,
            ) {
                Text(
                    stringResource(R.string.ok_),
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
    )
}

@Preview
@Composable
fun ErrorDialogPreview() {
    ErrorDialog("Error") {}
}