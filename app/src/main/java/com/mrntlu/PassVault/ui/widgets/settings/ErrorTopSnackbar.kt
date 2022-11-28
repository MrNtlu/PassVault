package com.mrntlu.PassVault.ui.widgets.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrntlu.PassVault.R

@Composable
fun ErrorTopSnackbar(
    error: String,
    onClick: () -> Unit,
) {
    //TODO Add isError boolean, if not error show gree and message

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.error),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .padding(start = 5.dp)
                .padding(end = 3.dp)
                .weight(1f),
            text = error,
            color = MaterialTheme.colorScheme.onError,
        )

        TextButton(
            onClick = onClick
        ) {
            Text(
                text = stringResource(id = R.string.ok),
                color = MaterialTheme.colorScheme.errorContainer,
            )
        }
    }
}

@Preview
@Composable
fun ErrorTopSnackbarPreview() {
    ErrorTopSnackbar(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris ac diam semper, hendrerit odio nec, convallis mauris. Nam non ipsum eu massa aliquam rhoncus vitae eu mauris"
    ) {}
}