package com.mrntlu.PassVault.ui.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mrntlu.PassVault.R

@Composable
fun ErrorDialog(
    error: String,
    onDismissClicked: () -> Unit,
) {
    CustomDialog(
        isConfirmButtonVisible = false,
        containerColor = MaterialTheme.colorScheme.error,
        iconContentColor = MaterialTheme.colorScheme.onError,
        icon = Icons.Rounded.Error,
        title = stringResource(R.string.error),
        titleColor = MaterialTheme.colorScheme.onError,
        text = {
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onError,
            )
        },
        onConfirmClicked = {},
        dismissTextColor = MaterialTheme.colorScheme.onError,
        dismissContainerColor = MaterialTheme.colorScheme.error,
        onDismissClicked = onDismissClicked,
    )
}

@Preview
@Composable
fun ErrorDialogPreview() {
    ErrorDialog("Error") {}
}