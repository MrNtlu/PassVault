package com.mrntlu.PassVault.ui.widgets

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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
        onDismissRequest = onDismissClicked,
        title = {
            Text(
                text = stringResource(R.string.ays),
                fontWeight = FontWeight.Medium
            )
        },
        text = {
            Text(text = text)
        },
        confirmButton = {
            Button(
                onClick = onConfirmClicked,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Gray,
                    contentColor = Color.White,
                )
            ) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissClicked,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red,
                    contentColor = Color.White
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