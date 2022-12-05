package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mrntlu.PassVault.R

@Composable
fun CustomDialog(
    containerColor: Color = MaterialTheme.colorScheme.background,
    iconContentColor: Color = MaterialTheme.colorScheme.onBackground,
    icon: ImageVector? = Icons.Rounded.Info,
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    text: @Composable (() -> Unit),
    confirmText: String = stringResource(id = R.string.yes),
    confirmTextColor: Color = MaterialTheme.colorScheme.onBackground,
    onConfirmClicked: () -> Unit,
    dismissText: String = stringResource(id = R.string.no),
    dismissContainerColor: Color = MaterialTheme.colorScheme.onBackground,
    dismissTextColor: Color = MaterialTheme.colorScheme.background,
    onDismissClicked: () -> Unit,
    isConfirmButtonVisible: Boolean = true
) {
    AlertDialog(
        onDismissRequest = onDismissClicked,
        containerColor = containerColor,
        iconContentColor = iconContentColor,
        icon = {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(R.string.cd_custom_dialog),
                )
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = titleColor,
            )
        },
        text = text,
        confirmButton = {
            if (isConfirmButtonVisible) {
                TextButton(
                    onClick = {
                        onDismissClicked()
                        onConfirmClicked()
                    },
                ) {
                    Text(
                        text = confirmText,
                        style = MaterialTheme.typography.labelLarge,
                        color = confirmTextColor,
                    )
                }
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = dismissContainerColor,
                )
            ) {
                Text(
                    text = if (isConfirmButtonVisible) dismissText else stringResource(id = R.string.ok),
                    style = MaterialTheme.typography.labelLarge,
                    color = dismissTextColor,
                )
            }
        },
    )
}

@Preview
@Composable
fun CustomDialogPreview() {
    CustomDialog(
        title = stringResource(id = R.string.info),
        text = {
            Column {
                Text(
                    text = stringResource(R.string.ads_dialog_info),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    TextButton(
                        onClick = {  },
                    ) {
                        Text(
                            text = stringResource(R.string.dont_show_again_),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        },
        onConfirmClicked = {},
        onDismissClicked = {},
    )
}