package com.mrntlu.PassVault.ui.widgets.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.ui.widgets.CustomDialog

@Composable
fun SettingsErrorDialog(
    text: String,
    onConfirmClicked: () -> Unit,
    onDismissClicked: () -> Unit,
) {
    CustomDialog(
        containerColor = MaterialTheme.colorScheme.error,
        iconContentColor = MaterialTheme.colorScheme.onError,
        icon = Icons.Rounded.Error,
        title = stringResource(R.string.error),
        titleColor = MaterialTheme.colorScheme.onError,
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onError,
            )
        },
        confirmText = stringResource(R.string.login),
        confirmTextColor = MaterialTheme.colorScheme.onError,
        onConfirmClicked = onConfirmClicked,
        dismissTextColor = MaterialTheme.colorScheme.onErrorContainer,
        dismissContainerColor = MaterialTheme.colorScheme.errorContainer,
        onDismissClicked = onDismissClicked,
    )
}

@Preview
@Composable
fun SettingsErrorDialogPreview() {
    SettingsErrorDialog(text = "Error", onConfirmClicked = {}, onDismissClicked = {})
}