package com.mrntlu.PassVault.ui.widgets

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mrntlu.PassVault.R

@Composable
fun AYSDialog(
    text: String,
    onConfirmClicked: () -> Unit,
    onDismissClicked: () -> Unit,
) {
    AlertDialog(
        backgroundColor = MaterialTheme.colorScheme.error,
        onDismissRequest = onDismissClicked,
        title = {
            Text(
                text = stringResource(R.string.ays),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onError,
            )
        },
        text = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onError,
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmClicked,
            ) {
                Text(
                    stringResource(R.string.yes),
                    color = MaterialTheme.colorScheme.onError,
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                )
            ) {
                Text(stringResource(R.string.no))
            }
        }
    )
}

@Preview
@Composable
fun AYSDialogPreview() {
    AYSDialog("Do you want to logout?", {}, {})
}