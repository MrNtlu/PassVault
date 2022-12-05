package com.mrntlu.PassVault.ui.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mrntlu.PassVault.R

@Composable
fun CautionDialog(
    text: String,
    onConfirmClicked: () -> Unit,
    onDismissClicked: () -> Unit,
) {
    CustomDialog(
        containerColor = MaterialTheme.colorScheme.error,
        iconContentColor = MaterialTheme.colorScheme.onError,
        icon = Icons.Rounded.Warning,
        title = stringResource(R.string.caution),
        titleColor = MaterialTheme.colorScheme.onError,
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onError,
            )
        },
        confirmTextColor = MaterialTheme.colorScheme.onError,
        onConfirmClicked = onConfirmClicked,
        dismissTextColor = MaterialTheme.colorScheme.onErrorContainer,
        dismissContainerColor = MaterialTheme.colorScheme.errorContainer,
        onDismissClicked = onDismissClicked,
    )
}

@Preview
@Composable
fun CautionDialogPreview() {
    CautionDialog(text = stringResource(id = R.string.ays_logout), onConfirmClicked =  {}, onDismissClicked = {})
}