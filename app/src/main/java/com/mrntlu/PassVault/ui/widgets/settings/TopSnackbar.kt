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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.ui.theme.Green700

@Composable
fun TopSnackbar(
    isError: Boolean,
    message: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isError) MaterialTheme.colorScheme.error
                else Green700
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .padding(start = 5.dp)
                .padding(end = 3.dp)
                .weight(1f),
            text = message,
            color = if (isError) MaterialTheme.colorScheme.onError else Color.White,
        )

        TextButton(
            onClick = onClick
        ) {
            Text(
                text = stringResource(id = R.string.ok),
                color = if (isError) MaterialTheme.colorScheme.errorContainer else Color.White,
            )
        }
    }
}

@Preview
@Composable
fun ErrorTopSnackbarPreview() {
    TopSnackbar(
        true,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris ac diam semper, hendrerit odio nec, convallis mauris. Nam non ipsum eu massa aliquam rhoncus vitae eu mauris"
    ) {}
}
@Preview
@Composable
fun TopSnackbarPreview() {
    TopSnackbar(
        false,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris ac diam semper, hendrerit odio nec, convallis mauris. Nam non ipsum eu massa aliquam rhoncus vitae eu mauris"
    ) {}
}